package team.pacify.bookeet.pager

import team.pacify.bookeet.data.models.finance.Sale

interface PagerInterface {
    fun onClick()
}

interface ResultPagerInterface {
    fun invoice(): Sale?
}