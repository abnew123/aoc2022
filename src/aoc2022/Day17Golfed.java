package aoc2022;
class Q{
int P[]={15,33666,66055,2113665,387},H[]={1,3,3,4,2},h,k,q;java.math.BigInteger c,B;
String s(boolean p,String[]j){
c=c.ZERO;h=q=0;var m=new java.util.HashMap();long n=p?2022:(long)1e12,r=0,e=0;
for(;r<n;){
B=c.valueOf(P[k=(int)(r++%5)]);int x=2,y=h+3,X;
do{
x=o(X=x+j[0].charAt(q)-61,y)?X:x;q=++q%j[0].length();
}while(o(x,y---1));
c=c.or(B.shiftLeft(x+7*++y));h=h<(y+=H[k])?y:h;
if(e<1){
X=h;
for(x=7;x-->0;X=y<0|y>=X?X:y+1)for(y=h;y-->0&&!c.testBit(7*y+x););
var v=(long[])m.put(q*5+k+":"+c.shiftRight(7*X),new long[]{r,h});
if(v!=null){long a=r-v[0],g=(n-r)/a;r+=g*a;e=g*(h-v[1]);}
}
}
return e+h+"";
}
boolean o(int x,int y){
return (x|y|7-x-H[(8-k)%5])>=0&c.and(B.shiftLeft(x+7*y)).signum()<1;
}
}
