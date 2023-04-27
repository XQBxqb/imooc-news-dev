package com.imooc.files.tt;

/**
 * @author 昴星
 * @date 2023-03-26 10:05
 * @explain
 */
public enum EnumMethod {

    SLOW(2){
        @Override
        public void run(int i){
            System.out.println("----"+i);
        }
    },
    FAST(3){
        @Override
        public void run(int i){
            System.out.println("----"+i);
        }
    },
    QUICK(4){
        @Override
        public void run(int i){
            System.out.println("----"+i);
        }
    };
     public final int status ;

       public abstract void run(int i);

     EnumMethod(int status){
        this.status=status;
    };

}
