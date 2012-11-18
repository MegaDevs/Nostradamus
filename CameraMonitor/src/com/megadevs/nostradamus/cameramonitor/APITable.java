package com.megadevs.nostradamus.cameramonitor;

public class APITable {
	
	
	public static int tot=4;
	public static int index=0;
	public static String[] getRekoApi(){
		index++;
		if(index>=4)index=0;
		
		switch (index){
		case 0:
			return new String[]{"F4NGyrk6HOOBN8Mb","G17UW0bfi09sh8mN"};
		case 1:
			return new String[]{"z667jUEQOanDLnnC","SC18dvttGElL0hPb"};
		case 2:
			return new String[]{"TyjvA356Wt93oVQS","2CiekB7AHvx5K6vG"};
		default:
			return new String[]{"59f6BxWq2i0gcrV7","S3OPyy6MOdcCUev3"};
		
		}
		
	}

}
