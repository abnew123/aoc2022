package aoc2022;
class Q{
int P[]={15,33666,66055,2113665,387},H[]={1,3,3,4,2},W[]={4,3,3,1,2},h,k;java.math.BigInteger c;
String s(boolean p,String[]j){
c=c.ZERO;h=0;var J=j[0];var m=$.h();long n=p?2022:0xe8d4a51000L,r=0,e=0;int q=0;
for(;r<n;){
k=$.f(r,5);int x=2,y=h+3,X;
for(;;){
X=x+J.charAt(q)-61;q=++q%J.length();
if(o(X,y))x=X;
if(o(x,y-1))y--;else{c=c.or(c.valueOf(P[k]).shiftLeft(x+7*y));h=$.x(h,y+H[k]);break;}
}
r++;if(e<1){
int f=h,i=7,z;
for(;i-->0;)for(z=h;z-->0;)if(c.testBit(7*z+i)){f=$.n(f,z+1);break;}
var v=(long[])m.put(r%5+","+q+":"+c.shiftRight(7*f),new long[]{r,h});
if(v!=null){long a=r-v[0],g=(n-r)/a;r+=g*a;e=g*(h-v[1]);}
}
}
return e+h+"";
}
boolean o(int x,int y){
return x>=0&y>=0&x+W[k]<8&c.and(c.valueOf(P[k]).shiftLeft(x+7*y)).signum()<1;
}
}
