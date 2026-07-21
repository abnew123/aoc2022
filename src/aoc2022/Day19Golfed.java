package aoc2022;
class S{int x(int u,int v){return u>v?u:v;}
int q[]=new int[12],m[],z;
String s(boolean p,String[]I){
int r=p?0:1,j=0,t=p?24:32;
for(var l:I)if(j++<3|p){for(int y=6;y-->0;)q[y<3?y*3:y*2+1]=new Integer(l.split("\\D+")[y+2]);m=new int[]{x(x(q[0],q[3]),x(q[6],q[9])),q[7],q[11],t};d(t,1,0,0,0,0,0,0,z=0);r=p?r+j*z:r*z;}
return""+r;
}
void d(int t,int...s){
if((z=x(z,s[7]))>=s[7]+t*t/2)return;
for(int i=4;i-->0;){
int w=s[i]<m[i]?0:t,j,y;
for(j=3;j-->0;)if((y=q[i*3+j]-s[4+j])>0)w=x(w,s[j]<1?t:~-y/s[j]+1);
if(w++<t){var u=s.clone();u[i]++;u[7]+=i/3*(t-w);for(j=3;j-->0;)u[4+j]+=s[j]*w-q[i*3+j];d(t-w,u);}
}
}
}
