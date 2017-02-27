package net.bohush.geometricprogressview.sample;

import android.app.Activity;
import android.os.Bundle;

import net.bohush.geometricprogressview.GeometricProgressView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GeometricProgressView progressView1 = (GeometricProgressView) findViewById(R.id.progressView1);
        progressView1.setNumberOfAngles(3);

        GeometricProgressView progressView2 = (GeometricProgressView) findViewById(R.id.progressView2);
        progressView2.setNumberOfAngles(4);

        GeometricProgressView progressView3 = (GeometricProgressView) findViewById(R.id.progressView3);
        progressView3.setNumberOfAngles(5);

        GeometricProgressView progressView4 = (GeometricProgressView) findViewById(R.id.progressView4);
        progressView4.setNumberOfAngles(6);

        GeometricProgressView progressView5 = (GeometricProgressView) findViewById(R.id.progressView5);
        progressView5.setNumberOfAngles(10);

        GeometricProgressView progressView6 = (GeometricProgressView) findViewById(R.id.progressView6);
        progressView6.setType(GeometricProgressView.TYPE.TRIANGLE);
        progressView6.setNumberOfAngles(3);

        GeometricProgressView progressView7 = (GeometricProgressView) findViewById(R.id.progressView7);
        progressView7.setType(GeometricProgressView.TYPE.TRIANGLE);
        progressView7.setNumberOfAngles(4);

        GeometricProgressView progressView8 = (GeometricProgressView) findViewById(R.id.progressView8);
        progressView8.setType(GeometricProgressView.TYPE.TRIANGLE);
        progressView8.setNumberOfAngles(5);

        GeometricProgressView progressView9 = (GeometricProgressView) findViewById(R.id.progressView9);
        progressView9.setType(GeometricProgressView.TYPE.TRIANGLE);
        progressView9.setNumberOfAngles(6);

        GeometricProgressView progressView10 = (GeometricProgressView) findViewById(R.id.progressView10);
        progressView10.setType(GeometricProgressView.TYPE.TRIANGLE);
        progressView10.setNumberOfAngles(10);

        GeometricProgressView progressView11 = (GeometricProgressView) findViewById(R.id.progressView11);
        progressView11.setType(GeometricProgressView.TYPE.KITE);
        progressView11.setNumberOfAngles(8);
        progressView11.setDuration(3000);

        GeometricProgressView progressView12 = (GeometricProgressView) findViewById(R.id.progressView12);
        progressView12.setType(GeometricProgressView.TYPE.TRIANGLE);
        progressView12.setFigurePaddingInDp(8);
        progressView12.setNumberOfAngles(6);

        GeometricProgressView progressView13 = (GeometricProgressView) findViewById(R.id.progressView13);
        progressView13.setType(GeometricProgressView.TYPE.KITE);
        progressView13.setDuration(500);
        progressView13.setNumberOfAngles(6);

        GeometricProgressView progressView14 = (GeometricProgressView) findViewById(R.id.progressView14);
        progressView14.setType(GeometricProgressView.TYPE.TRIANGLE);
        progressView14.setFigurePaddingInDp(8);
        progressView14.setNumberOfAngles(10);

        GeometricProgressView progressView15 = (GeometricProgressView) findViewById(R.id.progressView15);
        progressView15.setType(GeometricProgressView.TYPE.KITE);
        progressView15.setFigurePaddingInDp(1);
        progressView15.setNumberOfAngles(30);
    }
}
