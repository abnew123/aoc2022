package aoc2022;


class S{
int[]O,C,B,M;int z;java.util.Map S;
String s(boolean p,String[]I){
int r=p?0:1,j=0;
for(var l:I){
if(!p&j>2)break;
int v=f(l.split("\\D+"),p?24:32);j++;
r=p?r+j*v:r*v;
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
if(t<1|g+h*t+t*(t-1)/2<=z)return;
o=m(o,a,M[0],t);c=m(c,e,M[1],t);b=m(b,f,M[2],t);
long k=t|(long)a<<6|(long)e<<12|(long)f<<18|(long)h<<24|(long)o<<30|(long)c<<40|(long)b<<50;
if((int)S.getOrDefault(k,-1)>=g)return;
S.put(k,g);
int[]r={a,e,f,h};
for(int i=4;i-->0;){
if(r[i]>=M[i])continue;
int w=$.x(w(o,a,O[i]),$.x(w(c,e,C[i]),w(b,f,B[i])));
if(w<t){
int x=w+1;
d(t-x,a+(i==0?1:0),e+(i==1?1:0),f+(i==2?1:0),h+(i==3?1:0),o+a*x-O[i],c+e*x-C[i],b+f*x-B[i],g+h*x);
if(i==3&w<1)return;
}
}
}
int w(int h,int b,int c){return h>=c?0:b<1?99:(c-h+b-1)/b;}
int m(int h,int b,int m,int t){return $.n(h,$.x(0,m*t-b*(t-1)));}
}
