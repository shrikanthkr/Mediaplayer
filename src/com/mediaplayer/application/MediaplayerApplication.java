package com.mediaplayer.application;
import org.acra.*;
import org.acra.annotation.*;
import android.app.Application;

@ReportsCrashes(formKey = "", // will not be used
				mailTo = "reports@yourdomain.com",
				customReportContent = { ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT },                
				mode = ReportingInteractionMode.TOAST,
				resToastText = R.string.crash_toast_text)
public class MediaplayerApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        // The following line triggers the initialization of ACRA
        ACRA.init(this);
    }

}
