package team.pacify.bookeet.utils

object DbConstants {
    const val ENTRIES_PATH: String = "entries"
    const val TRANSACTIONS_PATH: String = "transactions"
    const val USERS_PATH: String = "users"
    const val PRODUCTS_PATH = "products"
    const val ACCOUNTS_PATH = "accounts"
    const val INVENTORIES_PATH = "inventories"
    const val CUSTOMERS_PATH = "customers"
    const val SUPPLIERS_PATH = "suppliers"
    const val SERVICES_PATH = "services"
    const val INVOICES_PATH = "invoices"
    const val SALES_PATH = "sales"
    const val CHATS_PATH = "chats"
}

object UIConstants {
    const val MIN_ITEM_IN_STORE = 0
    const val MAX_ITEM_IN_STORE = 99999999
    val ItemUnits = listOf(
        "Bag",
        "Pack",
        "Cup",
        "Congo",
        "Sack",
        "Container",
        "Sachet",
        "Bundle",
        "Kg",
        "Litre",
        "Dozen"
    )
    const val FIREBASE_LOAD_SIZE = 20L
}

