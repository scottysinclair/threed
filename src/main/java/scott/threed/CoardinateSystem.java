package scott.threed;

import java.util.List;

/**
 * Performs calculations, transforms / translates to different
 * coard systems.
 */
class CoardinateSystem {
    public Vector negate(Vector p) {
        return new Vector(p.getX() * -1f, p.getY() * -1f, p.getZ() * -1f);
    }
    public void transform(List<Vector> coards, Vector angle) {
        for (Vector coard: coards) {
            transform(coard, angle);
        }
    }

    private float toRadians(float angle) {
        return 0.0174532925F * angle;
    }

    public void transform(Vector coard, Vector angle) {
        //x and y points rotating around z axis
        float az = toRadians(angle.getZ());
        float pxx = (float)Math.cos(az) * coard.getX();
        float pxy = (float)Math.sin(az) * coard.getX();

        float pyx = (float)Math.sin(az) * coard.getY();
        float pyy = (float)Math.cos(az) * coard.getY();

        coard.setX(pxx + pyx);
        coard.setY(pyy - pxy);

        //x and z points rotating around y axis
        float ay = toRadians(angle.getY());
        pxx = (float)Math.cos(ay) * coard.getX();
        float pxz = (float)Math.sin(ay) * coard.getX();

        float pzx = (float)Math.sin(ay) * coard.getZ();
        float pzz = (float)Math.cos(ay) * coard.getZ();

        coard.setX(pxx + pzx);
        coard.setZ(pzz - pxz);

        //z and y points rotating around x axis
        float ax = toRadians(angle.getX());
        pzz = (float)Math.cos(ax) * coard.getZ();
        float pzy = (float)Math.sin(ax) * coard.getZ();

        float pyz = (float)Math.sin(ax) * coard.getY();
        pyy = (float)Math.cos(ax) * coard.getY();

        coard.setZ(pzz + pyz);
        coard.setY(pyy - pzy);
    }

    public void translate(List<Vector> coards, Vector pos) {
        for (Vector coard: coards) {
            translate(coard, pos);
        }
    }

    public Vector calculateSurfaceNormal(FaceCoards face) {
      Vector normal = new Vector(0f, 0f, 0f);

      for (int i=0, n=face.size(); i<n; i++) {
         Vector vCurrent = face.getCoard(i);
         Vector vNext = face.getCoard((i + 1) % n);

         normal.addX(  (vCurrent.getY() - vNext.getY()) *  (vCurrent.getZ() + vNext.getZ())   );
         normal.addY(  (vCurrent.getZ() - vNext.getZ()) *  (vCurrent.getX() + vNext.getX())  );
         normal.addZ(  (vCurrent.getX() - vNext.getX()) *  (vCurrent.getY() + vNext.getY())  );
      }
      return normalize(normal);
    }

    public Vector normalize(Vector vector) {
        //L = math.sqrt(V[0] * V[0] + V[1] * V[1] + V[2] * V[2])
        //V = (V[0] / L, V[1] / L, V[2] / L)
          double l = Math.sqrt((vector.getX() *  vector.getX()) + (vector.getY() * vector.getY()) + (vector.getZ() * vector.getZ()));
          return new Vector((float)(vector.getX() / l), (float)(vector.getY() / l), (float)(vector.getZ() / l));
        }


    public void translate(Vector coard, Vector pos) {
        coard.setX(coard.getX() + pos.getX());
        coard.setY(coard.getY() + pos.getY());
        coard.setZ(coard.getZ() + pos.getZ());
    }
}