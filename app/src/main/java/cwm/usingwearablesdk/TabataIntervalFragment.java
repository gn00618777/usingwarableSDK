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

public class TabataIntervalFragment extends Fragment {

    private View mView;

    private TextView intervalStartView;
    private TextView intervalCountView;
    private TextView intervalNextActionView;

    private String intervalCount = "";
    private String nextAction = "";
    private String actionComment = "";


    @Override
    public void onAttach(Context context){
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_tabata_interval, null);
        }
        intervalStartView = (TextView)mView.findViewById(R.id.interval_start_view);
        intervalCountView = (TextView)mView.findViewById(R.id.interval_count_view);
        intervalCountView.setText(intervalCount);
        intervalNextActionView = (TextView)mView.findViewById(R.id.prepare_action);
        intervalNextActionView.setText(nextAction+"/"+actionComment);

        return mView;
    }

    public void setIntervalCountView(String s){
        intervalCount = s;
    }
    public void setIntervalNextAction(String s){nextAction = s;}
    public void setIntervalActionComment(String s){actionComment = s;}



}
