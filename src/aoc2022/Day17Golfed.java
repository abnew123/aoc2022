package aoc2022;
class Q{
int P[]={387,15,33666,66055,2113665},h,k,q,y;java.math.BigInteger c,B;
String s(boolean p,String[]j){
var m=new java.util.HashMap();long n=p?2022:(long)1e12,r=0,e=0,a;
for(c=c.ZERO,q=-1;r++<n;){
B=c.valueOf(P[k=++k%5]);int x=2,X;y=h+3;
do x=o(X=x+j[0].charAt(q=++q%10091)-61)?X:x;while(o(x+--y-y));
X=h=((c=c.or(B.shiftLeft(x+7*++y))).bitLength()+6)/7;
for(x=0;X>0&x!=127;x|=(B=c.shiftRight(7*--X)).intValue()&127);
if(e<1&&m.put(q+":"+k+B,new long[]{r,h})instanceof long[]v){e=(n-r)/(a=r-v[0])*(h-v[1]);r=n-(n-r)%a;}
}
return e+h+"";
}
boolean o(int x){
return(x|y|(26909>>3*k&7)-x)>=0&c.and(B.shiftLeft(x+7*y)).signum()<1;
}
}
