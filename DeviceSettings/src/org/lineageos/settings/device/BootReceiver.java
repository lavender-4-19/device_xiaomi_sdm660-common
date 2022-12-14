/*
 * Copyright (C) 2018-2019 The Xiaomi-SDM660 Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.lineageos.settings.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import org.lineageos.settings.device.preferences.SecureSettingSwitchPreference;

import java.lang.Math.*;

public class BootReceiver extends BroadcastReceiver {

    public static final  String EARPIECE_GAIN_PATH = "/sys/kernel/sound_control/earpiece_gain";
    public static final  String HEADPHONE_GAIN_PATH = "/sys/kernel/sound_control/headphone_gain";
    public static final  String MIC_GAIN_PATH = "/sys/kernel/sound_control/mic_gain";
    
    public void onReceive(Context context, Intent intent) {
   
        // Audio Gain
        int gain = Settings.Secure.getInt(context.getContentResolver(),
                DeviceSettings.PREF_HEADPHONE_GAIN, 0);
        FileUtils.setValue(HEADPHONE_GAIN_PATH, gain + " " + gain);
        FileUtils.setValue(MIC_GAIN_PATH, Settings.Secure.getInt(context.getContentResolver(),
                DeviceSettings.PREF_MIC_GAIN, 0));
        FileUtils.setValue(EARPIECE_GAIN_PATH, Settings.Secure.getInt(context.getContentResolver(),
                DeviceSettings.PREF_EARPIECE_GAIN, 0));

        // Notification LED
        FileUtils.setValue(DeviceSettings.NOTIF_LED_PATH,(1 + Math.pow(1.05694, Settings.Secure.getInt(
                context.getContentResolver(), DeviceSettings.PREF_NOTIF_LED, 100))));

        // Vibration Strength
        FileUtils.setValue(DeviceSettings.VIBRATION_STRENGTH_PATH, Settings.Secure.getInt(
                context.getContentResolver(), DeviceSettings.PREF_VIBRATION_STRENGTH, 80) / 100.0 * (DeviceSettings.MAX_VIBRATION - DeviceSettings.MIN_VIBRATION) + DeviceSettings.MIN_VIBRATION);

        // Dirac
        context.startService(new Intent(context, DiracService.class));

        // FPS Info
        boolean enabled = Settings.Secure.getInt(context.getContentResolver(), 
                DeviceSettings.PREF_KEY_FPS_INFO, 0) == 1;
        if (enabled) {
            context.startService(new Intent(context, FPSInfoService.class));
        }
    }
}
