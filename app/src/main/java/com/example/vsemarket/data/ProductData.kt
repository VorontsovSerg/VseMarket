package com.example.vsemarket.data

object ProductData {
    val products = listOf(
        Product(
            id = 1,
            name = "Яблоко",
            price = 10.0,
            images = listOf(
                "https://images.unsplash.com/photo-1600585154340-be6161a56a0c",
                "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6"
            ),
            category = "Базар",
            subcategory = "Фрукты",
            description = "Кисло-сладкое зеленое яблоко с хрустящей мякотью, идеально для салатов и десертов.",
            attributes = mapOf(
                "Вес" to "150 г",
                "Калорийность" to "52 ккал/100 г",
                "Срок хранения" to "2 месяца"
            )
        ),
        Product(
            id = 2,
            name = "Груша",
            price = 12.0,
            images = listOf(
                "https://images.unsplash.com/photo-1606820732558-2eadae7c68e8",
                "https://images.unsplash.com/photo-1547517023-7ca0c162f816"
            ),
            category = "Базар",
            subcategory = "Фрукты",
            description = "Сочная и сладкая груша с мягкой текстурой, отличный перекус.",
            attributes = mapOf(
                "Вес" to "180 г",
                "Калорийность" to "57 ккал/100 г",
                "Срок хранения" to "1 месяц"
            )
        ),
        Product(
            id = 3,
            name = "Банан",
            price = 15.0,
            images = listOf(
                "https://images.unsplash.com/photo-1603834312919-4c11b54f75b0",
                "https://images.unsplash.com/photo-1571771894821-ce9b6c118b02"
            ),
            category = "Базар",
            subcategory = "Фрукты",
            description = "Спелый желтый банан, богатый калием, подходит для смузи и выпечки.",
            attributes = mapOf(
                "Вес" to "120 г",
                "Калорийность" to "89 ккал/100 г",
                "Срок хранения" to "3 недели"
            )
        ),
        Product(
            id = 4,
            name = "Апельсин",
            price = 18.0,
            images = listOf(
                "https://images.unsplash.com/photo-1611080626919-7cf5a9dbab5b",
                "https://images.unsplash.com/photo-1587735246935-5694508686a3"
            ),
            category = "Базар",
            subcategory = "Фрукты",
            description = "Сочный апельсин с ярким цитрусовым ароматом, идеален для сока.",
            attributes = mapOf(
                "Вес" to "200 г",
                "Калорийность" to "47 ккал/100 г",
                "Срок хранения" to "1.5 месяца"
            )
        ),
        Product(
            id = 5,
            name = "Киви",
            price = 20.0,
            images = listOf(
                "https://images.unsplash.com/photo-1584270451948-6b6b4053b2b0",
                "https://images.unsplash.com/photo-1612461277963-25a9b407498c"
            ),
            category = "Базар",
            subcategory = "Фрукты",
            description = "Кислый и сладкий киви с яркой зеленой мякотью, богат витамином C.",
            attributes = mapOf(
                "Вес" to "100 г",
                "Калорийность" to "61 ккал/100 г",
                "Срок хранения" to "2 месяца"
            )
        ),
        Product(
            id = 6,
            name = "Манго",
            price = 25.0,
            images = listOf(
                "https://images.unsplash.com/photo-1601493700631-7a7601f5bd1d",
                "https://images.unsplash.com/photo-1591375277687-4f2b4e04f3c8"
            ),
            category = "Базар",
            subcategory = "Фрукты",
            description = "Сладкий и ароматный манго, идеален для десертов и смузи.",
            attributes = mapOf(
                "Вес" to "300 г",
                "Калорийность" to "60 ккал/100 г",
                "Срок хранения" to "1 месяц"
            )
        ),
        Product(
            id = 7,
            name = "Ананас",
            price = 30.0,
            images = listOf(
                "https://images.unsplash.com/photo-1550258987-190a2d41a8ba",
                "https://images.unsplash.com/photo-1553692850-8a793611f36b"
            ),
            category = "Базар",
            subcategory = "Фрукты",
            description = "Сочный ананас с кисло-сладким вкусом, подходит для салатов и соков.",
            attributes = mapOf(
                "Вес" to "1 кг",
                "Калорийность" to "50 ккал/100 г",
                "Срок хранения" to "2 недели"
            )
        ),
        Product(
            id = 8,
            name = "Персик",
            price = 22.0,
            images = listOf(
                "https://images.unsplash.com/photo-1629828874514-5110a49a98c9",
                "https://images.unsplash.com/photo-1568584733360-6b1a1a6a7b7b"
            ),
            category = "Базар",
            subcategory = "Фрукты",
            description = "Мягкий и сладкий персик, отличный для перекуса или десертов.",
            attributes = mapOf(
                "Вес" to "150 г",
                "Калорийность" to "39 ккал/100 г",
                "Срок хранения" to "2 недели"
            )
        ),
        Product(
            id = 9,
            name = "Виноград",
            price = 28.0,
            images = listOf(
                "https://images.unsplash.com/photo-1599819179436-66404e3e689f",
                "https://images.unsplash.com/photo-1566842703329-a7df4e9e6364"
            ),
            category = "Базар",
            subcategory = "Фрукты",
            description = "Сладкий виноград, идеален для закусок и виноделия.",
            attributes = mapOf(
                "Вес" to "200 г",
                "Калорийность" to "69 ккал/100 г",
                "Срок хранения" to "1 месяц"
            )
        ),
        Product(
            id = 10,
            name = "Клубника",
            price = 35.0,
            images = listOf(
                "https://images.unsplash.com/photo-1587393855524-087f83d95b5b",
                "https://images.unsplash.com/photo-1518639192441-9b5b54595d1a"
            ),
            category = "Базар",
            subcategory = "Фрукты",
            description = "Сочная и сладкая клубника, подходит для десертов и смузи.",
            attributes = mapOf(
                "Вес" to "100 г",
                "Калорийность" to "32 ккал/100 г",
                "Срок хранения" to "1 неделя"
            )
        ),
        Product(
            id = 11,
            name = "Морковь",
            price = 8.0,
            images = listOf(
                "https://images.unsplash.com/photo-1598170845058-32b9d6a5da37",
                "https://images.unsplash.com/photo-1447175008436-054170c2e979"
            ),
            category = "Базар",
            subcategory = "Овощи",
            description = "Сладкая и хрустящая морковь, идеальна для супов и салатов.",
            attributes = mapOf(
                "Вес" to "150 г",
                "Калорийность" to "41 ккал/100 г",
                "Срок хранения" to "3 месяца"
            )
        ),
        Product(
            id = 12,
            name = "Картофель",
            price = 5.0,
            images = listOf(
                "https://images.unsplash.com/photo-1518977676601-b53f82aba655",
                "https://images.unsplash.com/photo-1592155931584-901ac81263e3"
            ),
            category = "Базар",
            subcategory = "Овощи",
            description = "Плотный картофель, подходит для жарки и пюре.",
            attributes = mapOf(
                "Вес" to "200 г",
                "Калорийность" to "77 ккал/100 г",
                "Срок хранения" to "6 месяцев"
            )
        ),
        Product(
            id = 13,
            name = "Лук репчатый",
            price = 6.0,
            images = listOf(
                "https://images.unsplash.com/photo-1618512496248-a07fe46126e6",
                "https://images.unsplash.com/photo-1603046899798-0bcea4bf0e09"
            ),
            category = "Базар",
            subcategory = "Овощи",
            description = "Острый и ароматный лук, незаменим в кулинарии.",
            attributes = mapOf(
                "Вес" to "120 г",
                "Калорийность" to "40 ккал/100 г",
                "Срок хранения" to "4 месяца"
            )
        ),
        Product(
            id = 14,
            name = "Томат",
            price = 12.0,
            images = listOf(
                "https://images.unsplash.com/photo-1592925625178-8a4e85b34b9e",
                "https://images.unsplash.com/photo-1601004890684-d8cbf643f5f2"
            ),
            category = "Базар",
            subcategory = "Овощи",
            description = "Сладкие томаты, идеальны для закусок и салатов.",
            attributes = mapOf(
                "Вес" to "20 г",
                "Калорийность" to "18 ккал/100 г",
                "Срок хранения" to "1 месяц"
            )
        ),
        Product(
            id = 15,
            name = "Огурец",
            price = 10.0,
            images = listOf(
                "https://images.unsplash.com/photo-1589621310934-4a9ab43382e9",
                "https://images.unsplash.com/photo-1587411681666-7aaa49d4eede"
            ),
            category = "Базар",
            subcategory = "Овощи",
            description = "Свежий и хрустящий огурец, отлично подходит для салатов.",
            attributes = mapOf(
                "Вес" to "150 г",
                "Калорийность" to "15 ккал/100 г",
                "Срок хранения" to "2 недели"
            )
        ),
        Product(
            id = 16,
            name = "Капуста",
            price = 7.0,
            images = listOf(
                "https://images.unsplash.com/photo-1598031811226-2354c4eb215b",
                "https://images.unsplash.com/photo-1603048297198-4f5f66b3f723"
            ),
            category = "Базар",
            subcategory = "Овощи",
            description = "Хрустящая капуста, подходит для салатов и тушения.",
            attributes = mapOf(
                "Вес" to "1 кг",
                "Калорийность" to "25 ккал/100 г",
                "Срок хранения" to "2 месяца"
            )
        ),
        Product(
            id = 17,
            name = "Перец",
            price = 15.0,
            images = listOf(
                "https://images.unsplash.com/photo-1613743983569-46e27f43a754",
                "https://images.unsplash.com/photo-1597843685481-5ea37727f9ea"
            ),
            category = "Базар",
            subcategory = "Овощи",
            description = "Сочный перец, идеален для салатов и гриля.",
            attributes = mapOf(
                "Вес" to "150 г",
                "Калорийность" to "40 ккал/100 г",
                "Срок хранения" to "1 месяц"
            )
        ),
        Product(
            id = 18,
            name = "Брокколи",
            price = 20.0,
            images = listOf(
                "https://images.unsplash.com/photo-1586941257178-0e787be074bb",
                "https://images.unsplash.com/photo-1628773826198-7374062f63e2"
            ),
            category = "Базар",
            subcategory = "Овощи",
            description = "Питательная брокколи, подходит для варки и запекания.",
            attributes = mapOf(
                "Вес" to "300 г",
                "Калорийность" to "35 ккал/100 г",
                "Срок хранения" to "2 недели"
            )
        ),
        Product(
            id = 19,
            name = "Кабачок",
            price = 9.0,
            images = listOf(
                "https://images.unsplash.com/photo-1596432529585-3b7d9f3b8e8b",
                "https://images.unsplash.com/photo-1601642231289-0f870d73f3ad"
            ),
            category = "Базар",
            subcategory = "Овощи",
            description = "Нежный кабачок, идеален для супов и жарки.",
            attributes = mapOf(
                "Вес" to "200 г",
                "Калорийность" to "17 ккал/100 г",
                "Срок хранения" to "1 месяц"
            )
        ),
        Product(
            id = 20,
            name = "Баклажан",
            price = 14.0,
            images = listOf(
                "https://images.unsplash.com/photo-1608709277818-3d66973e716b",
                "https://images.unsplash.com/photo-1597451693991-c360ed86e4eb"
            ),
            category = "Базар",
            subcategory = "Овощи",
            description = "Мясистый баклажан, подходит для запекания и рагу.",
            attributes = mapOf(
                "Вес" to "250 г",
                "Калорийность" to "25 ккал/100 г",
                "Срок хранения" to "1 месяц"
            )
        )
    )

    val categories = listOf(
        Category(
            name = "Базар",
            subcategories = listOf(
                Subcategory("Фрукты", 0xFF4CAF50),
                Subcategory("Овощи", 0xFF8BC34A)
            ),
            gradient = listOf(0xFF4CAF50, 0xFF8BC34A)
        )
    )
}