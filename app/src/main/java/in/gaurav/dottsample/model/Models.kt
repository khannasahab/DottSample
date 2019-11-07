package `in`.gaurav.dottsample.model

import com.google.gson.annotations.SerializedName


class FoursquareResponse() {
    @SerializedName("response")
    lateinit var response: Venues
}

class Venues {
    @SerializedName("venues")
    lateinit var venueList: List<Venue>
}

class Venue {
    @SerializedName("id")
    lateinit var id: String

    @SerializedName("name")
    lateinit var name: String

    @SerializedName("location")
    lateinit var location: Location


}

class Location(@SerializedName("lat") var lat: Double = 0.0, @SerializedName("lng") var lng: Double = 0.0) {
    @SerializedName("address")
    lateinit var address: String

    @SerializedName("city")
    lateinit var city: String


}

class Contact {

    @SerializedName("phone")
    lateinit var phone: String

    @SerializedName("twitter")
    lateinit var twitter: String
}