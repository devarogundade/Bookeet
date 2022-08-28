package team.pacify.bookeet.pager

import team.pacify.bookeet.data.models.finance.Invoice

interface PagerInterface {
    fun onClick()
}

interface InvoicePagerInterface {
    fun invoice(): Invoice?
}