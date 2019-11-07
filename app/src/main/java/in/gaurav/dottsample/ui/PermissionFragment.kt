package `in`.gaurav.dottsample.ui

import `in`.gaurav.dottsample.R
import `in`.gaurav.dottsample.util.REQUEST_CODE_LOCATION_PERMISSION
import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.permission_fragment.*


class PermissionFragment : Fragment() {
    companion object {
        fun getInstance() = PermissionFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.permission_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ok_button.setOnClickListener {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION_PERMISSION
            )
        }

        cancel_button.setOnClickListener {
            activity!!.finish()
        }

        setting_button.setOnClickListener {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", activity!!.getPackageName(), null)
            intent.data = uri
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(activity!!, R.string.unexpected_error, Toast.LENGTH_SHORT).show()
                activity!!.finish()
            }
        }
    }
}