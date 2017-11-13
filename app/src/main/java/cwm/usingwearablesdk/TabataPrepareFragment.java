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
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.util.Log;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import cwm.wearablesdk.TabataSettings;

public class TabataPrepareFragment extends Fragment {

    private View mView;

    private TextView prepareStartView;
    private TextView prepareCountView;
    private TextView prepareActionView;

    private String prepareStart = "Prepare";
    private String prepareCount;
    private String prepareAction = "";

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_tabata_prepare, null);
        }

        prepareStartView = (TextView)mView.findViewById(R.id.prepare_start_view);
        prepareCountView = (TextView)mView.findViewById(R.id.prepare_count_view);
        prepareActionView = (TextView)mView.findViewById(R.id.prepare_action);

        prepareStartView.setText(prepareStart);
        prepareCountView.setText(prepareCount);
        prepareActionView.setText(prepareAction);

        return mView;
    }

    public void setPrepareActionView(String s){prepareAction =s;}
    public void setPrepareCountView(String s){prepareCount = s;}



}
