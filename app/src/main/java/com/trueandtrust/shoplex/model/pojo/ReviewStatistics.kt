package com.trueandtrust.shoplex.model.pojo

data class ReviewStatistics(val oneStar:Int = 0,
                            val twoStars:Int = 0,
                            val threeStars:Int = 0,
                            val fourStars:Int = 0,
                            val fiveStars:Int = 0,
                            val rate: Float = 0F,
                            val total: Int = 0)