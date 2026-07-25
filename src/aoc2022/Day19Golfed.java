package aoc2022;
class S{int q[]=new int[16],z;
String s(boolean p,String[]I){
int j=0,t=p?24:32,r=t>>5;
for(var l:I)if(j++<3|p){for(int y=8;y-->2;)q[0xec984000>>y*4&15]=new Integer(l.split("\\D+")[y]);d(t,1,0,0,0,0,0,0,z=0);r=p?r+j*z:r*z;}
return""+r;
}
void d(int t,int...s){
for(int g=s[7]+s[3]*t,i=(z=z<g?g:z)<g+t*t/2?4:0;i-->0;){
int w=0,j,y;
for(j=4;j-->0;w=(y=q[i*4+j]-s[4+j])>0&w<(y=s[j]<1?t:~-y/s[j]+1)?y:w);
if(w++<t){var u=s.clone();u[i]++;for(j=4;j-->0;)u[4+j]+=s[j]*w-q[i*4+j];d(t-w,u);}
}
}
}
