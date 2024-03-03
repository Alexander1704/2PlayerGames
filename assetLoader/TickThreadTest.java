package assetLoader;

public class TickThreadTest{
    public TickThreadTest(){
        TickThread test = new TickThread(60, new Runnable(){
            @Override
            public void run(){
                System.out.println("test");
            }
        });
        test.start();
        
        FunctionLoader.warte(2000);
        test.finish();
    }
}