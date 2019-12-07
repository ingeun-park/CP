int max = 5000;

void main(){
   int a=1;
   int i;
   int test = 1;
   int b=99;
   add(a,b);
   int sum = 0;
   for (int i = 0; i <= 2; i++){
   	  sum = sum + i;
   }
   _print(sum);
}

int add(int x, int y){
   int z;
   z = x +y;
   _print(z);
   return z;
}

int sum(int n){
   int i =1;
   int sum = 0;
   while(i<n){
      sum = sum + i;
      i=i+1;
   }
   i = 3;
   return sum;
}

int sumFor(int n){
   int sum = 0;
   for(int i=0; i<= n; ++i){
      for(int j=0; j<=n; ++j){
         sum = sum +j;
      }
   }
   return sum;   
}
