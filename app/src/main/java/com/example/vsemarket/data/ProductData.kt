package com.example.vsemarket.data

/**
 * Вспомогательный класс или объект для управления товарами.
 * Может использоваться для хранения временных данных или моков.
 */
object ProductData {
    val products: MutableList<Product> = mutableListOf(
        // Категория: Базар
        Product(
            id = 1,
            name = "Яблоко",
            price = 10.0,
            images = listOf(
                "https://avatars.mds.yandex.net/i?id=ce0e1ec2b49fc9007e026aae1c6b60480bbae7d6-12610597-images-thumbs&n=13",
                "https://avatars.mds.yandex.net/i?id=09226b60ab39299434f46998e3d414bef23289fa-7930431-images-thumbs&ref=rim&n=33&w=200&h=200"
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
                "https://images.unsplash.com/photo-1577140920937-7c4c6d52f9d4",
                "https://images.unsplash.com/photo-1606820732558-2eadae7c68e8"
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
                "https://images.unsplash.com/photo-1612461277963-25a9b407498e"
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
        ),
        // Категория: Техника
        Product(
            id = 21,
            name = "Смартфон Galaxy S23",
            price = 79999.0,
            images = listOf(
                "https://images.unsplash.com/photo-1679404084864-6b58f6ce2b1e",
                "https://images.pexels.com/photos/1275229/pexels-photo-1275229.jpeg"
            ),
            category = "Техника",
            subcategory = "Смартфоны",
            description = "Современный смартфон с AMOLED-экраном, мощным процессором и тройной камерой.",
            attributes = mapOf(
                "Экран" to "6.1\" AMOLED",
                "Процессор" to "Snapdragon 8 Gen 2",
                "Камера" to "50 МП"
            )
        ),
        Product(
            id = 22,
            name = "Ноутбук MacBook Air M2",
            price = 119999.0,
            images = listOf(
                "https://images.unsplash.com/photo-1618410321329-1db6a1226e9b",
                "https://images.pexels.com/photos/812264/pexels-photo-812264.jpeg"
            ),
            category = "Техника",
            subcategory = "Ноутбуки",
            description = "Легкий и производительный ноутбук с чипом M2 и Retina-дисплеем.",
            attributes = mapOf(
                "Экран" to "13.6\" Retina",
                "Процессор" to "Apple M2",
                "Память" to "8 ГБ"
            )
        ),
        Product(
            id = 23,
            name = "Стиральная машина Bosch",
            price = 45999.0,
            images = listOf(
                "https://images.unsplash.com/photo-1626806819282-2c1dc55f9796",
                "https://images.pexels.com/photos/409701/pexels-photo-409701.jpeg"
            ),
            category = "Техника",
            subcategory = "Бытовая техника",
            description = "Надежная стиральная машина с функцией Eco Bubble и загрузкой 7 кг.",
            attributes = mapOf(
                "Загрузка" to "7 кг",
                "Скорость отжима" to "1200 об/мин",
                "Программы" to "15"
            )
        ),
        Product(
            id = 24,
            name = "Планшет iPad Pro 12.9",
            price = 109999.0,
            images = listOf(
                "https://images.unsplash.com/photo-1544244015-1a1f6834e4aa",
                "https://images.pexels.com/photos/106344/pexels-photo-106344.jpeg"
            ),
            category = "Техника",
            subcategory = "Планшеты",
            description = "Мощный планшет с Liquid Retina дисплеем и чипом M2.",
            attributes = mapOf(
                "Экран" to "12.9\" Liquid Retina",
                "Процессор" to "Apple M2",
                "Хранилище" to "256 ГБ"
            )
        ),
        Product(
            id = 25,
            name = "Умные часы Apple Watch Series 9",
            price = 39999.0,
            images = listOf(
                "https://images.unsplash.com/photo-1551816230-ef5deaed4a26",
                "https://images.pexels.com/photos/5081398/pexels-photo-5081398.jpeg"
            ),
            category = "Техника",
            subcategory = "Умные часы",
            description = "Умные часы с функциями отслеживания здоровья и ярким дисплеем.",
            attributes = mapOf(
                "Экран" to "Always-On Retina",
                "Водозащита" to "50 м",
                "Датчики" to "Пульс, ЭКГ"
            )
        ),
        Product(
            id = 26,
            name = "Беспроводные наушники Sony WH-1000XM5",
            price = 34999.0,
            images = listOf(
                "https://images.unsplash.com/photo-1618366712010-f33e676e3591",
                "https://images.pexels.com/photos/3780681/pexels-photo-3780681.jpeg"
            ),
            category = "Техника",
            subcategory = "Аудиотехника",
            description = "Наушники с активным шумоподавлением и высоким качеством звука.",
            attributes = mapOf(
                "Тип" to "Беспроводные",
                "Шумоподавление" to "Да",
                "Время работы" to "30 ч"
            )
        ),
        // Категория: Машины
        Product(
            id = 27,
            name = "Toyota Camry 2023",
            price = 2599000.0,
            images = listOf(
                "https://images.unsplash.com/photo-1502877338535-766e1452684a",
                "https://images.pexels.com/photos/170811/pexels-photo-170811.jpeg"
            ),
            category = "Машины",
            subcategory = "Автомобили",
            description = "Седан бизнес-класса с комфортным салоном и экономичным двигателем.",
            attributes = mapOf(
                "Двигатель" to "2.5 л",
                "Мощность" to "181 л.с.",
                "Трансмиссия" to "Автомат"
            )
        ),
        Product(
            id = 28,
            name = "Мотоцикл Yamaha MT-07",
            price = 799000.0,
            images = listOf(
                "https://images.unsplash.com/photo-1558981403-c5f9899a28bc",
                "https://images.pexels.com/photos/163319/pexels-photo-163319.jpeg"
            ),
            category = "Машины",
            subcategory = "Мотоциклы",
            description = "Маневренный мотоцикл с двигателем 689 см³, идеален для города.",
            attributes = mapOf(
                "Двигатель" to "689 см³",
                "Мощность" to "73 л.с.",
                "Вес" to "184 кг"
            )
        ),
        Product(
            id = 29,
            name = "Электромобиль Tesla Model 3",
            price = 3999000.0,
            images = listOf(
                "https://images.unsplash.com/photo-1561047021-4180b14a2593",
                "https://images.pexels.com/photos/168938/pexels-photo-168938.jpeg"
            ),
            category = "Машины",
            subcategory = "Электромобили",
            description = "Электромобиль с запасом хода 580 км и автопилотом.",
            attributes = mapOf(
                "Запас хода" to "580 км",
                "Мощность" to "480 л.с.",
                "Разгон 0-100" to "3.3 с"
            )
        ),
        Product(
            id = 30,
            name = "Land Rover Defender",
            price = 5999000.0,
            images = listOf(
                "https://images.unsplash.com/photo-1622180134679-8a3ae63bcdeb",
                "https://images.pexels.com/photos/1743366/pexels-photo-1743366.jpeg"
            ),
            category = "Машины",
            subcategory = "Внедорожники",
            description = "Мощный внедорожник для экстремальных условий.",
            attributes = mapOf(
                "Двигатель" to "3.0 л",
                "Мощность" to "400 л.с.",
                "Привод" to "Полный"
            )
        ),
        Product(
            id = 31,
            name = "Грузовик MAN TGS",
            price = 7999000.0,
            images = listOf(
                "https://images.unsplash.com/photo-1606728035253-855b8ed76792",
                "https://images.pexels.com/photos/2244746/pexels-photo-2244746.jpeg"
            ),
            category = "Машины",
            subcategory = "Грузовики",
            description = "Надежный грузовик для перевозки тяжелых грузов.",
            attributes = mapOf(
                "Грузоподъемность" to "20 т",
                "Двигатель" to "12.4 л",
                "Мощность" to "480 л.с."
            )
        ),
        Product(
            id = 32,
            name = "Автомобильный видеорегистратор",
            price = 9999.0,
            images = listOf(
                "https://images.unsplash.com/photo-1617043986173-3041a6528a13",
                "https://images.pexels.com/photos/3945683/pexels-photo-3945683.jpeg"
            ),
            category = "Машины",
            subcategory = "Аксессуары",
            description = "Компактный видеорегистратор с Full HD записью.",
            attributes = mapOf(
                "Разрешение" to "1080p",
                "Угол обзора" to "170°",
                "Память" to "32 ГБ"
            )
        ),
        // Категория: Услуги
        Product(
            id = 33,
            name = "Ремонт смартфонов",
            price = 5000.0,
            images = listOf(
                "https://images.unsplash.com/photo-1583573636246-18b21210386d",
                "https://images.pexels.com/photos/267394/pexels-photo-267394.jpeg"
            ),
            category = "Услуги",
            subcategory = "Ремонт техники",
            description = "Профессиональный ремонт смартфонов любой сложности.",
            attributes = mapOf(
                "Срок" to "1-2 дня",
                "Гарантия" to "3 месяца",
                "Тип" to "Диагностика + ремонт"
            )
        ),
        Product(
            id = 34,
            name = "Курсы английского языка",
            price = 15000.0,
            images = listOf(
                "https://images.unsplash.com/photo-1503676260728-90b8c9960367",
                "https://images.pexels.com/photos/614715/pexels-photo-614715.jpeg"
            ),
            category = "Услуги",
            subcategory = "Обучение",
            description = "Онлайн-курсы английского языка для начинающих и продвинутых.",
            attributes = mapOf(
                "Длительность" to "3 месяца",
                "Формат" to "Онлайн",
                "Уровень" to "A1-C1"
            )
        ),
        Product(
            id = 35,
            name = "Доставка грузов",
            price = 10000.0,
            images = listOf(
                "https://images.unsplash.com/photo-1586528116311-ad8dd3a8a7cc",
                "https://images.pexels.com/photos/2253831/pexels-photo-2253831.jpeg"
            ),
            category = "Услуги",
            subcategory = "Логистика",
            description = "Доставка грузов по России и СНГ с отслеживанием.",
            attributes = mapOf(
                "Вес" to "До 1000 кг",
                "Срок" to "3-7 дней",
                "Отслеживание" to "Да"
            )
        ),
        Product(
            id = 36,
            name = "Юридическая консультация",
            price = 3000.0,
            images = listOf(
                "https://images.unsplash.com/photo-1589829545856-d10d557cf95f",
                "https://images.pexels.com/photos/5668473/pexels-photo-5668473.jpeg"
            ),
            category = "Услуги",
            subcategory = "Консультации",
            description = "Консультация юриста по гражданским и административным делам.",
            attributes = mapOf(
                "Формат" to "Онлайн/Офлайн",
                "Длительность" to "1 час",
                "Специализация" to "Гражданское право"
            )
        ),
        Product(
            id = 37,
            name = "Уборка квартиры",
            price = 4000.0,
            images = listOf(
                "https://images.unsplash.com/photo-1581578735769-4fc3240cad77",
                "https://images.pexels.com/photos/4107286/pexels-photo-4107286.jpeg"
            ),
            category = "Услуги",
            subcategory = "Клининг",
            description = "Профессиональная уборка квартиры с экологичными средствами.",
            attributes = mapOf(
                "Площадь" to "До 100 м²",
                "Срок" to "3-4 часа",
                "Средства" to "Экологичные"
            )
        ),
        Product(
            id = 38,
            name = "Персональная тренировка",
            price = 2000.0,
            images = listOf(
                "https://images.unsplash.com/photo-1517836357463-d25dfeac3438",
                "https://images.pexels.com/photos/1552242/pexels-photo-1552242.jpeg"
            ),
            category = "Услуги",
            subcategory = "Фитнес",
            description = "Индивидуальная тренировка с профессиональным тренером.",
            attributes = mapOf(
                "Длительность" to "1 час",
                "Формат" to "В зале",
                "Уровень" to "Любой"
            )
        ),
        // Категория: Прочее
        Product(
            id = 39,
            name = "Набор для рисования",
            price = 2500.0,
            images = listOf(
                "https://images.unsplash.com/photo-1513366208864-8752b9bd302e",
                "https://images.pexels.com/photos/102127/pexels-photo-102127.jpeg"
            ),
            category = "Прочее",
            subcategory = "Хобби",
            description = "Набор для рисования, включающий краски, кисти и холст.",
            attributes = mapOf(
                "Тип" to "Акрил",
                "Количество" to "12 цветов",
                "Холст" to "30x40 см"
            )
        ),
        Product(
            id = 40,
            name = "Антикварный подсвечник",
            price = 15000.0,
            images = listOf(
                "https://images.unsplash.com/photo-1600585154340-be6161a56a0c",
                "https://images.pexels.com/photos/370717/pexels-photo-370717.jpeg"
            ),
            category = "Прочее",
            subcategory = "Антиквариат",
            description = "Старинный бронзовый подсвечник XIX века.",
            attributes = mapOf(
                "Материал" to "Бронза",
                "Период" to "XIX век",
                "Состояние" to "Отличное"
            )
        ),
        Product(
            id = 41,
            name = "Набор для пикника",
            price = 3500.0,
            images = listOf(
                "https://images.unsplash.com/photo-1496080174650-637e3f22fa03",
                "https://images.pexels.com/photos/2290070/pexels-photo-2290070.jpeg"
            ),
            category = "Прочее",
            subcategory = "Туризм",
            description = "Компактный набор для пикника на 4 персоны.",
            attributes = mapOf(
                "Материал" to "Пластик, текстиль",
                "Количество" to "4 персоны",
                "Вес" to "2 кг"
            )
        ),
        Product(
            id = 42,
            name = "Велосипед горный Trek",
            price = 45000.0,
            images = listOf(
                "https://images.unsplash.com/photo-1532298229144-4037532dc66e",
                "https://images.pexels.com/photos/489641/pexels-photo-489641.jpeg"
            ),
            category = "Прочее",
            subcategory = "Спорт",
            description = "Горный велосипед для активного отдыха и кросс-кантри.",
            attributes = mapOf(
                "Рама" to "Алюминий",
                "Колеса" to "29\"",
                "Скорости" to "21"
            )
        ),
        Product(
            id = 43,
            name = "Книга '1984' Дж. Оруэлл",
            price = 1500.0,
            images = listOf(
                "https://images.unsplash.com/photo-1544716278-ca5e3f4ebf0c",
                "https://images.pexels.com/photos/159866/pexels-photo-159866.jpeg"
            ),
            category = "Прочее",
            subcategory = "Книги",
            description = "Классический роман-антиутопия в твердом переплете.",
            attributes = mapOf(
                "Автор" to "Джордж Оруэлл",
                "Страниц" to "320",
                "Переплет" to "Твердый"
            )
        ),
        Product(
            id = 44,
            name = "Подарочный набор свечей",
            price = 2000.0,
            images = listOf(
                "https://images.unsplash.com/photo-1603002028539-ccc743b0e3e0",
                "https://images.pexels.com/photos/4020283/pexels-photo-4020283.jpeg"
            ),
            category = "Прочее",
            subcategory = "Подарки",
            description = "Набор ароматических свечей ручной работы.",
            attributes = mapOf(
                "Количество" to "4 шт",
                "Материал" to "Соевый воск",
                "Аромат" to "Лаванда, ваниль"
            )
        )
    )

    val categories: MutableList<Category> = mutableListOf(
        Category(
            name = "Базар",
            subcategories = listOf(
                Subcategory("Фрукты", 0xFF4CAF50),
                Subcategory("Овощи", 0xFF8BC34A)
            ),
            gradient = listOf(0xFF4CAF50, 0xFF8BC34A)
        ),
        Category(
            name = "Техника",
            subcategories = listOf(
                Subcategory("Смартфоны", 0xFF2196F3),
                Subcategory("Ноутбуки", 0xFF03A9F4),
                Subcategory("Бытовая техника", 0xFF00BCD4),
                Subcategory("Планшеты", 0xFF0097A7),
                Subcategory("Умные часы", 0xFF006064),
                Subcategory("Аудиотехника", 0xFF0077B6)
            ),
            gradient = listOf(0xFF2196F3, 0xFF00BCD4)
        ),
        Category(
            name = "Машины",
            subcategories = listOf(
                Subcategory("Автомобили", 0xFFE91E63),
                Subcategory("Мотоциклы", 0xFFF06292),
                Subcategory("Электромобили", 0xFFF48FB1),
                Subcategory("Внедорожники", 0xFFD81B60),
                Subcategory("Грузовики", 0xFFC2185B),
                Subcategory("Аксессуары", 0xFFAD1457)
            ),
            gradient = listOf(0xFFE91E63, 0xFFF48FB1)
        ),
        Category(
            name = "Услуги",
            subcategories = listOf(
                Subcategory("Ремонт техники", 0xFFFF9800),
                Subcategory("Обучение", 0xFFFFB300),
                Subcategory("Логистика", 0xFFFFCA28),
                Subcategory("Консультации", 0xFFFBC02D),
                Subcategory("Клининг", 0xFFF9A825),
                Subcategory("Фитнес", 0xFFF57F17)
            ),
            gradient = listOf(0xFFFF9800, 0xFFFFCA28)
        ),
        Category(
            name = "Прочее",
            subcategories = listOf(
                Subcategory("Хобби", 0xFF9C27B0),
                Subcategory("Антиквариат", 0xFFAB47BC),
                Subcategory("Туризм", 0xFFBA68C8),
                Subcategory("Спорт", 0xFF8E24AA),
                Subcategory("Книги", 0xFF7B1FA2),
                Subcategory("Подарки", 0xFF6A1B9A)
            ),
            gradient = listOf(0xFF9C27B0, 0xFFBA68C8)
        )
    )
}
