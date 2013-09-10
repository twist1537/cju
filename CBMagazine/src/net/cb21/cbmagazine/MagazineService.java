package net.cb21.cbmagazine;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MagazineService extends Service {
	// Constructor
	public MagazineService() {
	} // MagazineService()

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	} // onBind(Intent arg0)
} // MagazineService END