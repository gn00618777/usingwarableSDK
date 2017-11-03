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

    private Button actionItemtButton;
    private Button actionItemStartButton;

    private TextView actionItemView;
    private TextView actionItemStartView;

    private String actionItem="";
    private String actionItemStart="";

    // Container Activity must implement this interface
    public interface ListenForTabataActionItemFragment {
        public void onPressActionItemButton();
        public void onPressActionItemStartButton();
    }

    private ListenForTabataActionItemFragment mCallback;


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (ListenForTabataActionItemFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForTabataActionItemFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_tabata_action_item, null);
        }

        actionItemView = (TextView)mView.findViewById(R.id.action_item_view);
        actionItemView.setText(actionItem);
        actionItemStartView = (TextView)mView.findViewById(R.id.action_item_start_view);
        actionItemStartView.setText(actionItemStart);

        actionItemtButton = (Button)mView.findViewById(R.id.action_item);
        actionItemStartButton = (Button)mView.findViewById(R.id.action_item_start);

        actionItemtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressActionItemButton();
            }
        });

        actionItemStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressActionItemStartButton();
            }
        });




        return mView;
    }

    public void setActionItemView(String s){
        actionItem = s;
    }
    public void setActionItemStartView(String s){
        actionItemStart = s;
    }

}
