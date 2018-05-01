package cwm.usingwearablesdk;

/**
 * Created by user on 2017/9/10.
 */
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BitMapFragment extends Fragment {

    private View mView;
    private TextView mFileNameView;
    private TextView mFileSizeView;
    private TextView mFileStatusView;
    private Button mUploadButton;

    private boolean isEnable = false;

    ListenForBitMapFragment mCallback;

    // Container Activity must implement this interface
    public interface ListenForBitMapFragment {
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_bitmap, null);
        }

        mFileNameView = (TextView)mView.findViewById(R.id.file_name);
        mFileSizeView = (TextView)mView.findViewById(R.id.file_size);
        mFileStatusView = (TextView)mView.findViewById(R.id.file_status);
        mUploadButton = (Button)mView.findViewById(R.id.action_upload);

        return mView;
    }

    public void updateUI(String name, String size, String status){
        mFileNameView.setText(name);
        mFileSizeView.setText(size);
        mFileStatusView.setText(status);
    }

    public void enableUpload(boolean status){
        mUploadButton.setEnabled(status);
    }
}
