package com.example.bill_genrating_app.UtilClasses

import android.text.format.Formatter
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.bill_genrating_app.R
import org.jetbrains.annotations.TestOnly

fun ExtractDateFromOrdID(OrdId:String):String{
   val date = OrdId.subSequence(3,11).toString()
    val year = date.subSequence(0,4).toString()
    val month = date.subSequence(4,6).toString()
    val day = date.subSequence(6,8).toString()
    return "$day/$month/$year";
}

fun change_fragment(fragment: Fragment, container:Int,
                    pagename: String,
                    fragmentManager: FragmentManager,
                    isNavigatingForward: Boolean = true,
) {
    // Let's get the manager for handling our fragments
    val manager: FragmentManager = fragmentManager

    // Time to make the switch! We'll start a new transaction.
    manager.beginTransaction().apply {
        // Adding some snazzy animations for a smooth transition
        if (isNavigatingForward) {
            // When moving to a new screen (e.g., from left to right)
            setCustomAnimations(
                R.anim.slide_in_right,  // New fragment slides in from the right
                R.anim.slide_out_left,  // Old fragment slides out to the left
                R.anim.slide_in_left,   // When popping backstack, old fragment slides in from left
                R.anim.slide_out_right  // When popping backstack, new fragment slides out to right
            )
        } else {
            // When navigating back (e.g., from right to left)
            setCustomAnimations(
                R.anim.slide_in_left,   // New fragment slides in from the left
                R.anim.slide_out_right, // Old fragment slides out to the right
                R.anim.slide_in_right,  // When pushing to backstack (shouldn't happen here often)
                R.anim.slide_out_left   // When pushing to backstack (shouldn't happen here often)
            )
        }
        replace(container, fragment, pagename) // Replace the current fragment with the new one
        commit() // And... action! The change is now live.
    }
}
enum class status{
    PENDING,PAID
}
enum class FragementsName{
    SHOWITEMS,QRCODE,SHARE
}