/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.util.Scanner;

/**
 *
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;

    private final int NDATA = MAXVALUE / NTHREADS;
    private boolean isPaused = false;
    private PrimeFinderThread pft[];
    
    private Control() {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];

        int i;
        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA, this);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1, this);
    }
    
    public static Control newControl() {
        return new Control();
    }

    @Override
    public void run() {
        for(int i = 0;i < NTHREADS;i++ ) {
            pft[i].start();
        }
        Scanner scn = new Scanner(System.in);

        while (true) {
            try {
                Thread.sleep(TMILISECONDS);
                pauseThreads();
                
                int totalPrimes = countPrimes();
                System.out.println("Primes found: " + totalPrimes);
                System.out.println("Press 'Enter' to continue");
                scn.nextLine();

                boolean allFinished = true;
                for (int i = 0; i < NTHREADS; i++) {
                    if (pft[i].isAlive()) {
                        allFinished = false;
                        break;
                    }
                }

                if (allFinished){
                    System.out.println("\nHilitos are done");
                    break;
                }
                resumeThreads();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int totalPrimes = countPrimes();
        System.out.println("Total primes found: " + totalPrimes);
        scn.close();
    }

    public synchronized boolean isPaused(){
        return isPaused;
    }

    private synchronized void pauseThreads(){
        isPaused = true;
    }
    
    private synchronized void resumeThreads(){
        isPaused = false;
        notifyAll();
    }

    private int countPrimes(){
        int total = 0;
        for (int i = 0; i < NTHREADS; i++){
            total += pft[i].getPrimes().size();
        }
        return total;
    }
}
