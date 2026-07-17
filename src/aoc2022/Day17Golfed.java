package aoc2022;
import java.util.*;
class Q{
int P[]={15,132866,263175,16843009,771},h;byte c[];
String s(boolean p,String[]j){
c=new byte[1<<16];h=0;var J=j[0];var m=$.h();long n=p?2022:0xe8d4a51000L,r=0,e=0;int q=0;
for(;r<n;r++){
int k=$.f(r,5),x=2,y=h+3;
for(;;){
int X=x+(J.charAt(q)<61?-1:1);q=++q%J.length();
if(o(P[k],X,y))x=X;
if(o(P[k],x,y-1))y--;else{u(P[k],x,y);break;}
}
if(e<1){
var v=(long[])m.put(k(r+1,q),new long[]{r+1,h});
if(v!=null){long a=r+1-v[0],g=(n-r-1)/a;r+=g*a;e=g*(h-v[1]);}
}
}
return e+h+"";
}
boolean o(int p,int x,int y){
if((x|y)<0)return false;
for(;p>0;p>>=8,y++){
int w=p%256<<x;
if(w>127||y<c.length&&(w&c[y])>0)return false;
}
return true;
}
void u(int p,int x,int y){
for(;p>0;p>>=8,y++){
if(y==c.length)c=Arrays.copyOf(c,y*2);
int w=p%256<<x;c[y]|=w;h=$.x(h,y+1);
}
}
String k(long r,int q){
int f=h;
for(int x=7;x-->0;)for(int y=h;y-->0;)if((c[y]&1<<x)>0){f=$.n(f,y+1);break;}
return r%5+","+q+":"+new String(c,0,f,h-f);
}
}
