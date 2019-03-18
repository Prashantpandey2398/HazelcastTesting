package com.HazelcastTest;

import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
	
		
    public static void main( String[] args )
    {
    	LRUCache lru = new LRUCache();
    	while(true) {
    		System.out.println("Enter choice :");
    		Scanner sc = new Scanner(System.in);
    		int choice = sc.nextInt();
    		if(choice==1) {
    			lru.getCache(100000, "items_cache");
    		}
    		else if(choice==2) {
    			lru.insertitems();
    		}
    		else if(choice==3) {
    			lru.getitems();
    		}
    		else if(choice==4) {
    			break;
    		}
    		else {
    			System.out.println("Wrong option");
    		}
    	}
    }
}
