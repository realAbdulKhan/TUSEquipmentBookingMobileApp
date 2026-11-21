package com.example.mytusequipmentbooking.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mytusequipmentbooking.model.Equipment

@Composable
//  displays one piece of equipment as a card.
fun EquipmentCard(

    //equipment object containing its ID, name, category ID and description.
    equipment: Equipment,

    // category name is separate so it can be displayed on the card.
    categoryName: String,

    // Callback called when the user clicks the equipment details area.
    onOpen: () -> Unit,

    // Callback called when the user presses the Delete Equipment button.
    onDelete: () -> Unit,

    // Allows the parent screen to pass in its own layout modifications.
    // Modifier by default means no additional modifications are applied.
    modifier: Modifier = Modifier

) {

    // Card provides the layout for one equipment item.
    Card(
        modifier = modifier
            // Makes the card use the full available width of app.
            .fillMaxWidth()

            // Adds 8dp of spacing around the card.
            .padding(8.dp)

    ) {

        // Column arranges the card's contents vertically.
        Column(
            // Adds 16dp of internal spacing around the card content.
            modifier = Modifier.padding(16.dp)
        ) {

            // This inner column contains the clickable equipment details.
            Column(
                modifier = Modifier
                    // Makes the clickable area use the full available width of pp.
                    .fillMaxWidth()

                    // Makes the section clickable.
                    // When clicked, it calls onOpen, which is connected to
                    // the equipment details navigation in AppNavigation.
                    .clickable { onOpen() }

            ) {

                // Displays the equipment name on the card.
                Text(
                    text = equipment.name,

                    // Uses the medium title style from the theme.
                    style = MaterialTheme.typography.titleMedium
                )

                // Displays the category name under the equipment .
                Text(
                    text = categoryName,

                    // Uses the label medium text style.
                    style = MaterialTheme.typography.labelMedium
                )

                // Displays the  description.
                Text(
                    text = equipment.description,

                    // Uses the standard body text style.
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // button used to delete this equipment item.
            Button(
                // Calls the onDelete callback supplied by the parent screen.
                // The actual deletion is handled through the ViewModel and Repository.
                onClick = onDelete
            ) {

                // Text displayed inside the  button.
                Text("Delete Equipment")
            }
        }
    }
}