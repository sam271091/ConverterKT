package Apps.com.converterkt.composeScreens

import Apps.com.converterkt.composeViewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun BankBranchesMapScreen(viewModel: composeViewModel){
    val singapore = LatLng(1.35, 103.87)
    val baku = LatLng(40.409264, 49.867092)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(baku, 11f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {

        viewModel.bankBranches.forEach {
            var branchPosition = it.lat?.toDouble()?.let { it1 -> it.lng?.toDouble()
                ?.let { it2 -> LatLng(it1, it2) } }
            Marker(
                state = MarkerState(position = branchPosition as LatLng),
                title = it.branchName,
                snippet = it.vicinity
            )
        }

    }
}
