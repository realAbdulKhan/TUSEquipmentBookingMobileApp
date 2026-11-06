
package com.example.mytusequipmentbooking.data

import com.example.mytusequipmentbooking.model.Category
import com.example.mytusequipmentbooking.model.Equipment

object SampleData {

    val categories = listOf(
        Category(10, "Laptops"),
        Category(20, "Cameras"),
        Category(30, "Audio Equipment")
    )

    val equipment = listOf(
        Equipment(
            101,
            10,
            "Dell Latitude Laptop",
            "Dell business laptop suitable for programming and everyday college work."
        ),
        Equipment(
            102,
            10,
            "MacBook Pro",
            "Apple laptop suitable for software development and multimedia work."
        ),
        Equipment(
            201,
            20,
            "Canon EOS Camera",
            "Digital camera suitable for photography and video production."
        ),
        Equipment(
            202,
            20,
            "Sony Video Camera",
            "Video camera suitable for recording college projects and events."
        ),
        Equipment(
            301,
            30,
            "Rode Microphone",
            "External microphone suitable for audio recording and interviews."
        ),
        Equipment(
            302,
            30,
            "Wireless Headphones",
            "Wireless headphones suitable for audio monitoring and media work."
        )
    )
}
