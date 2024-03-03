package assetLoader;

import testClient.FPSCounter;

public class TickThreadTest{
    public TickThreadTest(){
        FPSCounter fpsCounter = new FPSCounter();
        TickThread test = new TickThread(60, new Runnable(){
            @Override
            public void run(){
                fpsCounter.update();
            }
        });
        test.run();
    }
}