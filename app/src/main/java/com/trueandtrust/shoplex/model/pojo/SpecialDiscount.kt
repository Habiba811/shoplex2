package com.trueandtrust.shoplex.model.pojo

import com.trueandtrust.shoplex.model.enumurations.DiscountType

data class SpecialDiscount(val discount: Float = 0F, val discountType: DiscountType = DiscountType.Fixed)