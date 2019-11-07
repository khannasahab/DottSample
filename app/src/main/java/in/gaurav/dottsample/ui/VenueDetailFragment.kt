package `in`.gaurav.dottsample.ui

import `in`.gaurav.dottsample.R
import `in`.gaurav.dottsample.model.Venue
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.venue_detail_fragment.*

class VenueDetailFragment : Fragment() {

    companion object {
        fun getInstance() = VenueDetailFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.venue_detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MapsActivity).mapViewModel.observeLastVenue(this, Observer {
            if (it == null) showError() else fillVenue(it!!)
        })
    }

    private fun fillVenue(venue: Venue) {
        try {
            venue_id.text = venue.id
            name.text = venue.name
            latlng.text = venue.location.lat.toString() + "," + venue.location.lng
            address.text = venue.location?.address
        } catch (e: UninitializedPropertyAccessException) {
            // TODO: handle the uninitialised location
            e.printStackTrace()
        }
    }

    private fun showError() {
        venue_id.text = getString(R.string.venue_not_selected)
        name.text = getString(R.string.venue_not_selected)
        address.text = getString(R.string.venue_not_selected)
    }
}