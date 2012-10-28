/** This class represents a 3D vector.
  */
import javax.media.opengl.*;

public class Vector3D {
    public double x,y,z;
    
    public Vector3D() {
	x = 0;
	y = 0;
	z = 0;
    }

    public Vector3D(double x, double y, double z) {
	this.x = x;
	this.y = y;
	this.z = z;
    }

    public Vector3D(Vector3D p) {
	x = p.x;
	y = p.y;
	z = p.z;
    }

    public Vector3D(float a[]) {
	if (a.length >= 3){
	    x = a[0];
	    y = a[1];
	    z = a[2];
	}
    }
    
    public Vector3D(double a[]) {
	if (a.length >= 3){
	    x = a[0];
	    y = a[1];
	    z = a[2];
	}
    }
    
    public double getX(){
	return x;
    }
    
    public double getY(){
	return y;
    }

    public double getZ(){
	return z;
    }
    
    //interpolate this + t*(q-this)
    public Vector3D interp(Vector3D q,double t){
	return new Vector3D(x + t*(q.x-x), y + t*(q.y-y), z + t*(q.z-z));
    }

    //subtract q from this point
    public Vector3D subtract(Vector3D q){
	return new Vector3D(x-q.x, y-q.y, z-q.z);
    }

    //add q from this point
    public Vector3D add(Vector3D q){
	return new Vector3D(x+q.x, y+q.y, z+q.z);
    }

    //multiply by a scalar
    public Vector3D scale(double s){
	return new Vector3D(s*x, s*y, s*z);
    }

    //rotate about x axis
    public Vector3D rotatex(double ang){
	return new Vector3D(x,Math.cos(ang)*y - Math.sin(ang)*z, Math.sin(ang)*y + Math.cos(ang)*z);
    }

    //rotate about y axis
    public Vector3D rotatey(double ang){
	return new Vector3D(Math.cos(ang)*x + Math.sin(ang)*z, y, -Math.sin(ang)*x + Math.cos(ang)*z);
    }

    //rotate about z axis
    public Vector3D rotatez(double ang){
	return new Vector3D(Math.cos(ang)*x - Math.sin(ang)*y, Math.sin(ang)*x + Math.cos(ang)*y,z);
    }

    //length of vector
    public double length(){
	return Math.sqrt(x*x+y*y+z*z);
    }

    //make it length one
    public Vector3D normalize(){
	return scale(1/length());
    }

    //dot product
    public double dot(Vector3D q){
	return x*q.x + y*q.y + z*q.z;
    }

    //cross product
    public Vector3D cross(Vector3D q){
	return new Vector3D(y*q.z - z*q.y,
			    z*q.x - x*q.z,
			    x*q.y - y*q.x);
    }

    //for converting to spherical coordinates
    //think of earth with the y axis going from South to North pole
    //(note that Maths texts have this as the z axis instead) 
    //azimuth is the latitude (in radians)
    public double azimuth() {
	return Math.atan2(z,x);
    }
    //elevation is the longitude (in radians)
    public double elevation() {
        return Math.acos(y/length());
    }

    //convert to Cartesian coordinates from spherical (r,azimuth,elevation)
    public static Vector3D fromSpherical(double r, double azimuth, double elevation){
	return new Vector3D(r*Math.sin(elevation)*Math.cos(azimuth),
			    r*Math.cos(elevation),
			    r*Math.sin(elevation)*Math.sin(azimuth));
    }



    public String toString(){
	return x+" "+y+" "+z;
    }
    
    //this does exactly the same thing as gluLookat, except that it takes
    //three vectors
    public static void lookAt(GL gl, Vector3D eye, Vector3D centre, Vector3D up) {
	Vector3D n = eye.subtract(centre).normalize();
        Vector3D u = up.cross(n).normalize();
        Vector3D v = n.cross(u);
	/* OpenGL matrices are in column major order */
        double[] m = {u.x, v.x, n.x, 0,
		      u.y, v.y, n.y, 0,
		      u.z, v.z, n.z, 0,
		      0,   0  , 0  , 1};
       
        /* do the rotation*/
	gl.glMultMatrixd(m,0);

	/* Translate Eye to Origin */
	gl.glTranslated(-eye.x, -eye.y, -eye.z);
    }

    //Apply local to world transform given basis and origin

    public static void localToWorld(GL gl, Vector3D u, Vector3D v, Vector3D n, Vector3D O) {
	/* OpenGL matrices are in column major order */
        double[] m = {u.x, u.y, u.z, 0,
		      v.x, v.y, v.z, 0,
		      n.x, n.y, n.z, 0,
		      O.x, O.y, O.z, 1};

        /* apply the transform*/
	gl.glMultMatrixd(m,0);
    }

}
