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
    const val REQUESTS_PATH = "requests"
}

object FSIConstants {
    const val FSI_BASE_URL = "https://bookeet.herokuapp.com/api/"
    const val CONN_TIMEOUT = 60L
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
    const val SENDER_VIEW_TYPE = 100
    const val RECEIVER_VIEW_TYPE = 101
}

