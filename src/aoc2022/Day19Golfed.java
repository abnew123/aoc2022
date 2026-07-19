package aoc2022;
class S{
int q[],m[],z;
String s(boolean p,String[]I){
int r=p?0:1,j=0,t=p?24:32;
for(var l:I)if(j++<3|p){var x=l.split("\\D+");q=new int[]{$.i(x[2]),0,0,$.i(x[3]),0,0,$.i(x[4]),$.i(x[5]),0,$.i(x[6]),0,$.i(x[7])};m=new int[]{$.x($.x(q[0],q[3]),$.x(q[6],q[9])),q[7],q[11],t};z=0;d(t,1,0,0,0,0,0,0,0);r=p?r+$.i(x[1])*z:r*z;}
return""+r;
}
void d(int t,int a,int e,int f,int h,int o,int c,int b,int g){
z=$.x(z,g+h*t);if(g+h*t+t*~-t/2<=z)return;
int[]r={a,e,f,h},v={o,c,b};
for(int i=4;i-->0;)if(r[i]<m[i]){
int w=0,k=i*3;
for(int j=3;j-->0;)if(v[j]<q[k+j])w=$.x(w,r[j]<1?t:(q[k+j]-v[j]+r[j]-1)/r[j]);
r[i]++;if(w++<t)d(t-w,r[0],r[1],r[2],r[3],o+a*w-q[k],c+e*w-q[k+1],b+f*w-q[k+2],g+h*w);r[i]--;
}
}
}
