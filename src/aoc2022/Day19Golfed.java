package aoc2022;import java.util.*;


class S{
int[]O,C,B,M;int z;Map S;
String s(boolean p,String[]I){
int r=p?0:1,j=0;
for(var l:I)if(j++<3|p){
var x=l.split("\\D+");int v=f(x,p?24:32);
r=p?r+$.i(x[1])*v:r*v;
}
return""+r;
}
int f(String[]x,int t){
int a=$.i(x[2]),e=$.i(x[3]),f=$.i(x[4]),h=$.i(x[5]),o=$.i(x[6]),c=$.i(x[7]);
O=new int[]{a,e,f,o};C=new int[]{0,0,h,0};B=new int[]{0,0,0,c};
M=new int[]{$.x($.x(a,e),$.x(f,o)),h,c,t};z=0;S=$.h();
d(t,1,0,0,0,0,0,0,0);return z;
}
void d(int t,int a,int e,int f,int h,int o,int c,int b,int g){
z=$.x(z,g+h*t);
if(g+h*t+t*(t-1)/2<=z)return;
o=m(o,a,M[0],t);c=m(c,e,M[1],t);b=m(b,f,M[2],t);
var k=List.of(t,a,e,f,h,o,c,b);
if((int)S.getOrDefault(k,-1)>=g)return;S.put(k,g);
int[]r={a,e,f,h};
for(int i=4;i-->0;)if(r[i]<M[i]&o>=O[i]&c>=C[i]&b>=B[i]){
d(t-1,a+(i==0?1:0),e+(i==1?1:0),f+(i==2?1:0),h+(i==3?1:0),o+a-O[i],c+e-C[i],b+f-B[i],g+h);
if(i==3)return;
}
d(t-1,a,e,f,h,o+a,c+e,b+f,g+h);
}
int m(int h,int b,int m,int t){return $.n(h,m*t-b*t+b);}
}
