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

public class TabataActionItemFragment extends Fragment {

    private View mView;


    private TextView actionItemView;
    private TextView actionItemStartView;

    private String actionItem="";
    private String actionItemStart="";


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_tabata_action_item, null);
        }

        actionItemView = (TextView)mView.findViewById(R.id.action_item_view);
        actionItemView.setText("Action Item: "+actionItem);
        actionItemStartView = (TextView)mView.findViewById(R.id.action_item_start_view);
        actionItemStartView.setText(actionItemStart);


        return mView;
    }

    public void setActionItemView(String s){
        actionItem = s;
    }
    public void setActionItemStartView(String s){
        actionItemStart = s;
    }

}
