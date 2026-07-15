package aoc2022;
class S{
int q[][],m[],z;
String s(boolean p,String[]I){
int r=p?0:1,j=0;
for(var l:I)if(j++<3|p){var x=l.split("\\D+");int v=f(x,p?24:32);r=p?r+$.i(x[1])*v:r*v;}
return""+r;
}
int f(String[]x,int t){
int a=$.i(x[2]),e=$.i(x[3]),f=$.i(x[4]),h=$.i(x[5]),o=$.i(x[6]),c=$.i(x[7]);
q=new int[][]{{a,0,0},{e,0,0},{f,h,0},{o,0,c}};m=new int[]{$.x($.x(a,e),$.x(f,o)),h,c,t};z=0;
d(t,1,0,0,0,0,0,0,0);return z;
}
void d(int t,int a,int e,int f,int h,int o,int c,int b,int g){
z=$.x(z,g+h*t);if(g+h*t+t*(t-1)/2<=z)return;
int[]r={a,e,f,h},v={o,c,b};
for(int i=4;i-->0;)if(r[i]<m[i]){
int w=0;
for(int j=3;j-->0;)if(v[j]<q[i][j])w=$.x(w,r[j]<1?t:(q[i][j]-v[j]+r[j]-1)/r[j]);
if(w++<t)d(t-w,a+(i==0?1:0),e+(i==1?1:0),f+(i==2?1:0),h+(i==3?1:0),o+a*w-q[i][0],c+e*w-q[i][1],b+f*w-q[i][2],g+h*w);
}
}
}
