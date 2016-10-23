package com.dji.sdk.sample.gimbal;

import android.app.Service;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dji.sdk.sample.R;

import dji.sdk.Gimbal.DJIGimbal;
import dji.sdk.SDKManager.DJISDKManager;
import dji.sdk.base.DJIBaseComponent;
import dji.sdk.base.DJIBaseProduct;
import dji.sdk.base.DJIError;
import dji.sdk.util.DJIParamCapability;
import dji.sdk.util.DJIParamMinMaxCapability;
public class GimbalCapabilityView extends LinearLayout implements View.OnClickListener {

    protected static final String TAG = "GimbalCapabilityView";

    private static DJIBaseProduct mProduct = null;
    private int i=0;
    private Button mPitchMinBtn;
    private Button mPitchMaxBtn;
    private Button mYawMinBtn;
    private Button mYawMaxBtn;
    private Button mRollMinBtn;
    private Button mRollMaxBtn;
    private Button mResetBtn;
    private Button mTestBtn;
    private TextView mTextView;
    private TextView mTextView2;
    private TextView mTextView3;
    private Button mPitchmBtn;
    private Button mPitchpBtn;
    private Button mYawmBtn;
    private Button mYawpBtn;
    private Button mRollmBtn;
    private Button mRollpBtn;

    //TextView textView= (TextView) findViewById(R.id.text1);
    //TextView tv = (TextView) findViewById(R.id.textView);
    private DJIGimbal.DJIGimbalAngleRotation mPitchRotation;
    private DJIGimbal.DJIGimbalAngleRotation mYawRotation;
    private DJIGimbal.DJIGimbalAngleRotation mRollRotation;

    protected Context mContext;

    public GimbalCapabilityView(Context context, AttributeSet attrs) {

        super(context, attrs);

        initUI();
        setupButtonsState();
        setupRotationStructs();
        enablePitchExtensionIfPossible();

        getGimbalInstance().setGimbalWorkMode(DJIGimbal.DJIGimbalWorkMode.YawFollowMode, new DJIBaseComponent.DJICompletionCallback() {
            @Override
            public void onResult(DJIError error) {
                if (error == null) {
                    Log.d("Gimbal", "Set Gimbal Work Mode success");
                } else {
                    Log.d("Gimbal", "Set Gimbal Work Mode failed" + error);
                }
            }
        });

    }

    public synchronized DJIBaseProduct getProductInstance()
    {
        while (null == mProduct) {
            if (DJISDKManager.getInstance() != null)
            {
                mProduct = DJISDKManager.getInstance().getDJIProduct();
            }
        }
        return mProduct;
    }

    private DJIGimbal getGimbalInstance()
    {
        return getProductInstance().getGimbal();
    }

    /*
     * Check if The Gimbal Capability is supported
     */
    private boolean isFeatureSupported(DJIGimbal.DJIGimbalCapabilityKey key) {

        DJIGimbal gimbal = getGimbalInstance();
        if (gimbal == null){
            return false;
        }

        DJIParamCapability capability = gimbal.gimbalCapability.get(key);
        if(capability != null){
            return capability.isSuppported();
        }
        return false;
    }

    private void setupButtonsState() {
        mPitchMinBtn.setEnabled(isFeatureSupported(DJIGimbal.DJIGimbalCapabilityKey.AdjustPitch));
        mPitchMaxBtn.setEnabled(isFeatureSupported(DJIGimbal.DJIGimbalCapabilityKey.AdjustPitch));
        mTestBtn.setEnabled(isFeatureSupported(DJIGimbal.DJIGimbalCapabilityKey.AdjustPitch));
        mYawMinBtn.setEnabled(isFeatureSupported(DJIGimbal.DJIGimbalCapabilityKey.AdjustYaw));
        mYawMaxBtn.setEnabled(isFeatureSupported(DJIGimbal.DJIGimbalCapabilityKey.AdjustYaw));
        mRollMinBtn.setEnabled(isFeatureSupported(DJIGimbal.DJIGimbalCapabilityKey.AdjustRoll));
        mRollMaxBtn.setEnabled(isFeatureSupported(DJIGimbal.DJIGimbalCapabilityKey.AdjustRoll));
        mPitchmBtn.setEnabled(isFeatureSupported(DJIGimbal.DJIGimbalCapabilityKey.AdjustPitch));
        mPitchpBtn.setEnabled(isFeatureSupported(DJIGimbal.DJIGimbalCapabilityKey.AdjustPitch));
        mYawmBtn.setEnabled(isFeatureSupported(DJIGimbal.DJIGimbalCapabilityKey.AdjustYaw));
        mYawpBtn.setEnabled(isFeatureSupported(DJIGimbal.DJIGimbalCapabilityKey.AdjustYaw));
        mRollmBtn.setEnabled(isFeatureSupported(DJIGimbal.DJIGimbalCapabilityKey.AdjustRoll));
        mRollpBtn.setEnabled(isFeatureSupported(DJIGimbal.DJIGimbalCapabilityKey.AdjustRoll));
    }

    private void setupRotationStructs() {

        mPitchRotation.enable = isFeatureSupported(DJIGimbal.DJIGimbalCapabilityKey.AdjustPitch);
        mYawRotation.enable = isFeatureSupported(DJIGimbal.DJIGimbalCapabilityKey.AdjustYaw);
        mRollRotation.enable = isFeatureSupported(DJIGimbal.DJIGimbalCapabilityKey.AdjustRoll);

    }

    private void enablePitchExtensionIfPossible() {

        DJIGimbal gimbal = getGimbalInstance();
        if (gimbal == null){
            return;
        }
        boolean ifPossible = isFeatureSupported(DJIGimbal.DJIGimbalCapabilityKey.PitchRangeExtension);
        if (ifPossible)
        {
            gimbal.setPitchRangeExtensionEnabled(true,
                    new DJIBaseComponent.DJICompletionCallback() {
                        @Override
                        public void onResult(DJIError djiError) {
                            if (djiError == null) {
                                Log.d("PitchRangeExtension", "set PitchRangeExtension successfully");
                            }else{
                                Log.d("PitchRangeExtension", "set PitchRangeExtension failed");
                            }
                        }
                    }

            );
        }

    }

    private void sendRotateGimbalCommand() {

        DJIGimbal gimbal = getGimbalInstance();
        if (gimbal == null){
            return;
        }


        gimbal.rotateGimbalByAngle(DJIGimbal.DJIGimbalRotateAngleMode.AbsoluteAngle, mPitchRotation, mRollRotation, mYawRotation,
                new DJIBaseComponent.DJICompletionCallback() {
                    @Override
                    public void onResult(DJIError djiError) {
                        if (djiError == null) {
                            Log.d("RotateGimbal", "RotateGimbal successfully");
                        }else{
                            Log.d("PitchRangeExtension", "RotateGimbal failed");
                        }
                    }
                }
        );
    }

    private Object getCorrespondingKeyWithButton(Button button){
//        if (button == mPitchMaxBtn || button == mPitchMinBtn || button ==mTestBtn){
        if (button == mPitchMaxBtn || button == mPitchMinBtn || button ==mTestBtn || button ==mPitchmBtn || button ==mPitchpBtn){
            return DJIGimbal.DJIGimbalCapabilityKey.AdjustPitch;
//        }else if (button == mYawMaxBtn || button == mYawMinBtn){
        }else if (button == mYawMaxBtn || button == mYawMinBtn || button == mYawmBtn || button == mYawpBtn){
            return DJIGimbal.DJIGimbalCapabilityKey.AdjustYaw;
//        }else if (button == mRollMaxBtn || button == mRollMinBtn){
        }else if (button == mRollMaxBtn || button == mRollMinBtn || button == mRollmBtn || button == mRollpBtn){
            return DJIGimbal.DJIGimbalCapabilityKey.AdjustRoll;
        }
        return null;
    }

    private void initUI() {



        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);

        View content = layoutInflater.inflate(R.layout.view_gimbal_capability, null, false);
        addView(content, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        mPitchMinBtn = (Button)findViewById(R.id.btn_pitchMin);
        mPitchMaxBtn = (Button)findViewById(R.id.btn_pitchMax);
        mYawMinBtn = (Button)findViewById(R.id.btn_yawMin);
        mYawMaxBtn = (Button)findViewById(R.id.btn_yawMax);
        mRollMinBtn = (Button)findViewById(R.id.btn_rollMin);
        mRollMaxBtn = (Button)findViewById(R.id.btn_rollMax);
        mResetBtn = (Button)findViewById(R.id.btn_reset);
        mTestBtn =(Button)findViewById(R.id.btn_test);
        mTextView = (TextView)findViewById(R.id.textView);
        mTextView2 = (TextView)findViewById(R.id.textView2);
        mTextView3 = (TextView)findViewById(R.id.textView3);
        mPitchmBtn = (Button)findViewById(R.id.btn_pitchm5);
        mPitchpBtn = (Button)findViewById(R.id.btn_pitchp5);
        mYawmBtn = (Button)findViewById(R.id.btn_yawm5);
        mYawpBtn = (Button)findViewById(R.id.btn_yawp5);
        mRollmBtn = (Button)findViewById(R.id.btn_rollm5);
        mRollpBtn = (Button)findViewById(R.id.btn_rollp5);

        mPitchMinBtn.setOnClickListener(this);
        mPitchMaxBtn.setOnClickListener(this);
        mYawMinBtn.setOnClickListener(this);
        mYawMaxBtn.setOnClickListener(this);
        mRollMinBtn.setOnClickListener(this);
        mRollMaxBtn.setOnClickListener(this);
        mResetBtn.setOnClickListener(this);
        mTestBtn.setOnClickListener(this);
        mPitchmBtn.setOnClickListener(this);
        mPitchpBtn.setOnClickListener(this);
        mYawmBtn.setOnClickListener(this);
        mYawpBtn.setOnClickListener(this);
        mRollmBtn.setOnClickListener(this);
        mRollpBtn.setOnClickListener(this);


        mPitchRotation = new DJIGimbal.DJIGimbalAngleRotation(false, 0, DJIGimbal.DJIGimbalRotateDirection.Clockwise);
        mYawRotation = new DJIGimbal.DJIGimbalAngleRotation(false, 0, DJIGimbal.DJIGimbalRotateDirection.Clockwise);
        mRollRotation = new DJIGimbal.DJIGimbalAngleRotation(false, 0, DJIGimbal.DJIGimbalRotateDirection.Clockwise);
    }

    private void rotateGimbalRoll(boolean rollEnable, float angle, DJIGimbal.DJIGimbalRotateDirection direction) {

        DJIGimbal.DJIGimbalAngleRotation pitch = new DJIGimbal.DJIGimbalAngleRotation(rollEnable, angle,
                DJIGimbal.DJIGimbalRotateDirection.Clockwise);
        DJIGimbal.DJIGimbalAngleRotation roll = new DJIGimbal.DJIGimbalAngleRotation(false, 0,
                DJIGimbal.DJIGimbalRotateDirection.Clockwise);
        DJIGimbal.DJIGimbalAngleRotation yaw = new DJIGimbal.DJIGimbalAngleRotation(false, 0, DJIGimbal.DJIGimbalRotateDirection.Clockwise);
        getGimbalInstance().rotateGimbalByAngle(DJIGimbal.DJIGimbalRotateAngleMode.AbsoluteAngle, pitch, roll, yaw, new DJIBaseComponent.DJICompletionCallback() {
            @Override
            public void onResult(DJIError error) {
                if (error == null) {
                    Log.d("RotateAngle", "Rotate Gimbal Roll Success");
                } else {
                    Log.d("RotateAngle", "Rotate Gimbal Roll fail" + error);
                }
            }
        });
    }

    private void rotateGimbalToMin (Button button) {

        DJIGimbal gimbal = getGimbalInstance();
        if (gimbal == null){
            return;
        }

        Object key = getCorrespondingKeyWithButton(button);
        Number minValue = ((DJIParamMinMaxCapability)(gimbal.gimbalCapability.get(key))).getMin();
        //textView.setText("hi");
        if (key == DJIGimbal.DJIGimbalCapabilityKey.AdjustPitch){
            mPitchRotation.direction = DJIGimbal.DJIGimbalRotateDirection.Clockwise;
            mPitchRotation.angle = minValue.floatValue();
//            textView.setText("MinPitch : "+mPitchRotation.angle);
        }else if(key == DJIGimbal.DJIGimbalCapabilityKey.AdjustYaw){
            mYawRotation.direction = DJIGimbal.DJIGimbalRotateDirection.Clockwise;
            mYawRotation.angle = minValue.floatValue();
//            textView.setText("MinYaw : "+mYawRotation.angle);
        }else if(key == DJIGimbal.DJIGimbalCapabilityKey.AdjustRoll){
            mRollRotation.direction = DJIGimbal.DJIGimbalRotateDirection.Clockwise;
            mRollRotation.angle = minValue.floatValue();
//            textView.setText("MinRoll : "+mRollRotation.angle);
        }

        sendRotateGimbalCommand();
    }

    private void rotateGimbalToMax(Button button) {

        DJIGimbal gimbal = getGimbalInstance();
        if (gimbal == null){
            return;
        }

        Object key = getCorrespondingKeyWithButton(button);
        Number maxValue = ((DJIParamMinMaxCapability)(gimbal.gimbalCapability.get(key))).getMax();
        //textView.setText("bye");
        if (key == DJIGimbal.DJIGimbalCapabilityKey.AdjustPitch){
            mPitchRotation.direction = DJIGimbal.DJIGimbalRotateDirection.Clockwise;
            mPitchRotation.angle = maxValue.floatValue();
//            textView.setText("MaxPitch : "+mPitchRotation.angle);
        }else if(key == DJIGimbal.DJIGimbalCapabilityKey.AdjustYaw){
            mYawRotation.direction = DJIGimbal.DJIGimbalRotateDirection.Clockwise;
            mYawRotation.angle = maxValue.floatValue();
 //           textView.setText("MaxYaw : "+mYawRotation.angle);
        }else if(key == DJIGimbal.DJIGimbalCapabilityKey.AdjustRoll){
            mRollRotation.direction = DJIGimbal.DJIGimbalRotateDirection.Clockwise;
            mRollRotation.angle = maxValue.floatValue();
 //           textView.setText("MaxRoll : "+mRollRotation.angle);
        }

        sendRotateGimbalCommand();
    }
    private void rotateGimbalfive(int sel) {
        DJIGimbal gimbal = getGimbalInstance();
        if (gimbal == null) {
            return;
        }
        if(sel == -1){
            mPitchRotation.direction = DJIGimbal.DJIGimbalRotateDirection.Clockwise;
            mPitchRotation.angle = mPitchRotation.angle-5;
        }else if(sel == 1){
            mPitchRotation.direction = DJIGimbal.DJIGimbalRotateDirection.Clockwise;
            mPitchRotation.angle = mPitchRotation.angle+5;
        }else if(sel == -2){
            mYawRotation.direction = DJIGimbal.DJIGimbalRotateDirection.Clockwise;
            mYawRotation.angle = mYawRotation.angle-5;
        }else if(sel == 2){
            mYawRotation.direction = DJIGimbal.DJIGimbalRotateDirection.Clockwise;
            mYawRotation.angle = mYawRotation.angle+5;
        }else if(sel == -3){
            mRollRotation.direction = DJIGimbal.DJIGimbalRotateDirection.Clockwise;
            mRollRotation.angle = mRollRotation.angle-5;
        }else if(sel == 3){
            mRollRotation.direction = DJIGimbal.DJIGimbalRotateDirection.Clockwise;
            mRollRotation.angle = mRollRotation.angle+5;
        }
        sendRotateGimbalCommand();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_pitchm5:{
                rotateGimbalfive(-1);
                mTextView.setText("Rotate Gimbal's Pitch : "+mPitchRotation.angle);
                break;
            }
            case R.id.btn_pitchp5:{
                rotateGimbalfive(1);
                mTextView.setText("Rotate Gimbal's Pitch : "+mPitchRotation.angle);
                break;
            }
            case R.id.btn_yawm5:{
                rotateGimbalfive(-2);
                mTextView2.setText("Rotate Gimbal's Yaw : "+mYawRotation.angle);
                break;
            }
            case R.id.btn_yawp5:{
                rotateGimbalfive(2);
                mTextView2.setText("Rotate Gimbal's Yaw : "+mYawRotation.angle);
                break;
            }
            case R.id.btn_rollm5:{
                rotateGimbalfive(-3);
                mTextView3.setText("Rotate Gimbal's Roll : "+mRollRotation.angle);
                break;
            }
            case R.id.btn_rollp5:{
                rotateGimbalfive(3);
                mTextView3.setText("Rotate Gimbal's Roll : "+mRollRotation.angle);
                break;
            }
            case R.id.btn_pitchMin:{
                rotateGimbalToMin((Button)v);
               //textView.setText("hi");
               // textView.setText("Res : "+mPitchRotation);
                mTextView.setText("Rotate Gimbal's Pitch : "+mPitchRotation.angle);
                break;
            }
            case R.id.btn_pitchMax:{
                rotateGimbalToMax((Button) v);
                //textView.setText("Res : "+mPitchRotation);
                mTextView.setText("Rotate Gimbal's Pitch : "+mPitchRotation.angle);
                break;
            }
            case R.id.btn_yawMin:{
                rotateGimbalToMin((Button) v);
                //textView.setText("Res : "+mYawRotation);
                mTextView2.setText("Rotate Gimbal's Yaw : "+mYawRotation.angle);
                break;
            }
            case R.id.btn_yawMax:{
                rotateGimbalToMax((Button) v);
                //textView.setText("Res : "+mYawRotation);
                mTextView2.setText("Rotate Gimbal's Yaw : "+mYawRotation.angle);
                break;
            }
            case R.id.btn_rollMin:{
                rotateGimbalToMin((Button) v);
                //textView.setText("Res : "+mRollRotation);
                mTextView3.setText("Rotate Gimbal's Roll : "+mRollRotation.angle);
                break;
            }
            case R.id.btn_rollMax:{
                rotateGimbalToMax((Button) v);
                //textView.setText("Res : "+mRollRotation);
                mTextView3.setText("Rotate Gimbal's Roll : "+mRollRotation.angle);
                break;
            }
            case R.id.btn_reset:{
                mPitchRotation.angle = 0;
                mYawRotation.angle = 0;
                mRollRotation.angle = 0;
                sendRotateGimbalCommand();
                //textView = (TextView) findViewById(R.id.text1) ;
                //textView.setText("Res : "+mPitchRotation+" "+mYawRotation+" "+mRollRotation);
                break;
            }
            case R.id.btn_test: {

                for (int i = 0; i < 10; i++){
                    try {
                        Thread.sleep(3000);
                        mPitchRotation.angle = (float) -Math.random()*90;
                        mYawRotation.angle = 0;
                        mRollRotation.angle = 0;
                        sendRotateGimbalCommand();
                        //textView = (TextView) findViewById(R.id.text1) ;
                        //textView.setText("Res : "+mPitchRotation+" "+mYawRotation+" "+mRollRotation);
                    }
                    catch(InterruptedException e){
                    }
                    finally {

                    }
                }
                break;
            }
            default:
                break;
        }
    }


}
