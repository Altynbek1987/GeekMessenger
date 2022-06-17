package com.geektechkb.feature_main.domain.models

class Userr {
    var name: String = " "
    var surname: String = " "
    var phoneNumber: String = " "
    var profileImage: String = " "

    constructor()
    constructor(name: String, surname: String, phoneNumber: String, profileImage: String) {
        this.name = name
        this.surname = surname
        this.phoneNumber = phoneNumber
        this.profileImage = profileImage

    }
}