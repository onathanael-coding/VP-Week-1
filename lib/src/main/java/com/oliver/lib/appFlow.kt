package com.oliver.lib
import java.util.ArrayList
import java.util.Scanner

class appFlow {

    val menuList =  ArrayList<FoodItem>()
    val orderList = ArrayList<Order>()
    val scanner = Scanner(System.`in`)

    init {
        menuList.add(FoodItem(1, "Nasi Goreng", 15000))
        menuList.add(FoodItem(2, "Mie Goreng", 15000))
        menuList.add(FoodItem(3, "Es Teh", 5000))
    }

    fun jalan() {

        var running = true
        while (running) {
            println("ORDER SYSTEM")
            println("1. Make order\n" +
                    "2. View Orders\n" +
                    "3. View Menu\n" +
                    "4. Add Menu\n" +
                    "5. Edit Menu\n" +
                    "6. Delete Menu\n" +
                    "7. Exit\n")
            print("Choose: ")

            when(scanner.nextLine().trim()) {
                "1" -> makeOrder()
                "2" -> viewOrder()
                "3" -> viewMenu()
                "4" -> addMenu()
                "5" -> editMenu()
                "6" -> deleteMenu()
                "7" -> {
                    println("Thank you")
                    running = false
                }
                else -> println("Invalid Input Again")
            }
            println("")
        }
    }

    fun makeOrder() {
        println("Make Order")
        println("Nama Customer: ")
        val customerName = scanner.nextLine().trim()
        if (customerName.isBlank()) {
            println("Tidak Boleh Kosong")
            return
        }

        val selectedItems = ArrayList<OrderDetail>()
        while (true) {
            viewMenu()
            print("Masukkan ID Menu ('0' untuk selesai): ")
            val id = scanner.nextLine().trim().toIntOrNull()
            if (id == null) continue
            if (id == 0) break

            var food: FoodItem? = null
            for (item in menuList) {
                if (item.id == id) {
                    food = item
                    break
                }
            }

            if (food == null) {
                println("Menu tidak ada")
                continue
            }

            print("Jumlah porsi untuk ${food.name}: ")
            val qty = scanner.nextLine().trim().toIntOrNull()
            if (qty == null || qty <= 0) {
                println("Jumlah Porsi minimal 1")
                continue
            }

            selectedItems.add(OrderDetail(food, qty))
            println("Ditambahkan: ${food.name} x $qty")
        }

        if (selectedItems.isNotEmpty()) {
            val order = Order((1000..9999).random(), customerName, selectedItems)
            orderList.add(order)
            println("Pesanan Dibuat!")
            printOrderFormat(order)
        } else {
            println("Pesanan dibatalkan.")
        }
    }

    fun viewOrder() {
        println("VIEW ORDERS")
        if (orderList.isEmpty()) {
            println("Belum ada pesanan")
        } else {
            for (order in orderList) {
                printOrderFormat(order)
            }
        }
    }

    fun viewMenu() {
        println("DAFTAR MENU")
        if (menuList.isEmpty()) {
            println("Menu kosong")
        } else {
            for (item in menuList) {
                println("${item.id}. ${item.name} - Rp${item.price}")
            }
        }
    }

    fun addMenu() {
        println("ADD MENU")
        print("Nama makanan: ")
        val name = scanner.nextLine().trim()
        if (name.isBlank()) {
            println("Nama tidak boleh kosong")
            return
        }

        print("Harga: ")
        val priceInput = scanner.nextLine().trim()
        val price = priceInput.toIntOrNull()
        if (price == null || price <= 0) {
            println("Harga tidak boleh negatif")
            return
        }

        var maxId = 0
        for (item in menuList) {
            if (item.id > maxId) {
                maxId = item.id
            }
        }

        menuList.add(FoodItem(maxId + 1, name, price))
        println("Menu '$name' berhasil ditambahkan!")
    }

    fun editMenu() {
        println("EDIT MENU")
        viewMenu()
        if (menuList.isEmpty()) return

        print("Masukkan ID menu yang mau diedit: ")
        val id = scanner.nextLine().trim().toIntOrNull()
        if (id == null) {
            println("ID harus berupa angka!")
            return
        }

        var foundItem: FoodItem? = null
        for (item in menuList) {
            if (item.id == id) {
                foundItem = item
                break
            }
        }

        if (foundItem == null) {
            println("Menu tidak ditemukan")
            return
        }

        print("Nama baru: ")
        val newName = scanner.nextLine().trim()
        if (newName.isNotBlank()) {
            foundItem.name = newName
        }

        print("Harga baru: ")
        val priceInput = scanner.nextLine().trim()
        val newPrice = priceInput.toIntOrNull()
        if (newPrice != null && newPrice > 0) {
            foundItem.price = newPrice
        } else {
            println("Harga tidak valid, Menggunakan harga lama.")
        }

        println("Berhasil Diupdate!")
    }

    fun deleteMenu() {
        println("DELETE MENU")
        viewMenu()
        if (menuList.isEmpty()) return

        print("Masukkan ID menu yang mau dihapus: ")
        val id = scanner.nextLine().trim().toIntOrNull()
        if (id == null) {
            println("ID harus berupa angka")
            return
        }

        // Cari item yang mau dihapus
        var itemToRemove: FoodItem? = null
        for (item in menuList) {
            if (item.id == id) {
                itemToRemove = item
                break
            }
        }

        if (itemToRemove != null) {
            menuList.remove(itemToRemove)
            println("Menu '${itemToRemove.name}' Berhasil Dihapus!")
        } else {
            println("ID ndak ada nyo")
        }
    }

    private fun printOrderFormat(order: Order) {
        println("\n${order.customerName}'s ORDER")

        var index = 1
        for (item in order.items) {
            println("$index. ${item.foodItem.name} x${item.quantity}")
            println("Rp${item.totalPrice}")
            index++
        }

        println("TOTAL")
        println("Rp${order.totalPrice}")
    }
}



