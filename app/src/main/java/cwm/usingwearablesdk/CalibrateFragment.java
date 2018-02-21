package cwm.usingwearablesdk;

/**
 * Created by user on 2017/9/10.
 */
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.util.Log;
import android.widget.TextView;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import cwm.wearablesdk.TabataSettings;

public class CalibrateFragment extends Fragment {

    private View mView;

    private Button startKADXL;
    private Button startKBMI160;

    private ListView messageView;
    private ArrayAdapter<String> listAdapter;


    // Container Activity must implement this interface
    public interface ListenForCommandTestFragment {
        public void onPressCalibrateADXL();
        public void onPressCalibrateBMI160();

    }

    private ListenForCommandTestFragment mCallback;


    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mCallback = (ListenForCommandTestFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ListenForCommandTestFragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.layout_test, null);
        }
        Log.d("bernie","onCreatee");
        messageView = (ListView)mView.findViewById(R.id.list_view);
        listAdapter = new ArrayAdapter<String>(getContext(), R.layout.message_detail);
        messageView.setAdapter(listAdapter);


        startKADXL = (Button)mView.findViewById(R.id.start_calibrate_adxl);
        startKBMI160 = (Button)mView.findViewById(R.id.start_calibrate_bmi160);

        startKADXL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressCalibrateADXL();
            }
        });
        startKBMI160.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPressCalibrateBMI160();
            }
        });


        return mView;
    }

    public void setRawByte(byte[] rawByte){
        try {
            String msg = new String(rawByte, "UTF-8");
            listAdapter.add(msg);
            messageView.smoothScrollToPosition(listAdapter.getCount() - 1);
        }catch (IOException e){

        }
        try {
            File file = new File(Environment.getExternalStorageDirectory().toString() + "/Download/CwmLog.txt");
            FileWriter txt = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(txt);
            String log = new String(rawByte, "UTF-8");
            bw.write(log);
            bw.newLine();
            bw.close();
        }catch (IOException e){

        }

    }

}
