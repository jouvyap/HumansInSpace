package com.bravostudio.humansinspace

import com.google.gson.annotations.SerializedName

data class MainDataModel(
    @SerializedName("message" ) var message : String = "",
    @SerializedName("people"  ) var people : ArrayList<Astronaut> = arrayListOf(),
    @SerializedName("number"  ) var number : Int = 0
)

data class Astronaut(
    @SerializedName("name" ) var name : String = "",
    @SerializedName("craft") var craft : String = "",
)