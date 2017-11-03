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

    private Button prepareStartButton;
    private Button prepareCountButton;
    private Button prepareEndButton;

    private TextView prepareStartView;
    private TextView prepareCountView;
    private TextView prepareEndView;

    private String prepareStart;
    private String prepareCount;
    private String prepareEnd;

    // Container Activity must implement this interface
    public interface ListenForTabataPrepareFragment {
        public void onPressPrepareStartButton();
        public void onPressPrepareCountButton();
        public void onPressPrepareEndButton();

    }

    private ListenForTabataPrepareFragment mCallback;


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (ListenForTabataPrepareFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForTabataPrepareFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_tabata_prepare, null);
        }

        prepareStartButton = (Button)mView.findViewById(R.id.prepare_start);
        prepareStartView = (TextView)mView.findViewById(R.id.prepare_start_view);

        prepareCountButton = (Button)mView.findViewById(R.id.prepare_count);
        prepareCountView = (TextView)mView.findViewById(R.id.prepare_count_view);

        prepareEndButton = (Button)mView.findViewById(R.id.prepare_end);

        prepareStartView.setText(prepareStart);
        prepareCountView.setText(prepareCount);


        prepareStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressPrepareStartButton();
            }
        });
        prepareCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressPrepareCountButton();
            }
        });
        prepareEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressPrepareEndButton();
            }
        });

        return mView;
    }

    public void setPrepareStartView(String s){
        prepareStart = s;
    }
    public void setPrepareCountView(String s){
        prepareCount = s;
    }



}
