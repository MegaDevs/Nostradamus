package com.megadevs.nostradamus.nostratooth.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class CommonStorage {
	
	public File dataFile = null;
	
	protected Object load(Object obj) {
		Class cls = obj.getClass();
		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(dataFile));
			obj = cls.cast(is.readObject());
			if (obj == null) {
				obj = cls.newInstance();
			}
			is.close();
		} catch (Exception e) {}
		finally {
			try {
				if (obj == null) {
					obj = cls.newInstance();
				}
			} catch (Exception e) {}
		}
		return obj;
	}
	
	protected synchronized void save(Object obj) {
		try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(dataFile));
			os.writeObject(obj);
			os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
