package comparators;

public class Tolerance {
    private double epsilon;
    private double d;

    public Tolerance(double d) {
        this.d = d;
        epsilon = 0;
        getEpsilon();
    }

    public double getEpsilon() {
        if (isPeta(d)) {
            epsilon = Math.pow(10, 12);
        } else if (isTera(d)) {
            epsilon = Math.pow(10, 9);
        } else if (isGiga(d)) {
            epsilon = Math.pow(10, 6);
        } else if (isMega(d)) {
            epsilon = Math.pow(10, 3);
        } else if (isKilo(d)) {
            epsilon = Math.pow(10, 2);
        } else if (isHecto(d)) {
            epsilon = Math.pow(10, 1);
        } else if (isDeka(d)) {
            epsilon = Math.pow(10, 0);
        } else if (isBaseUnit(d)) {
            epsilon = Math.pow(10, -1);
        } else if (isDeci(d)) {
            epsilon = Math.pow(10, -2);
        } else if (isCenti(d)) {
            epsilon = Math.pow(10, -3);
        } else if (isMilli(d)) {
            epsilon = Math.pow(10, -6);
        } else if (isMicro(d)) {
            epsilon = Math.pow(10, -9);
        } else if (isNano(d)) {
            epsilon = Math.pow(10, -10);
        } else if (isAngstrom(d)) {
            epsilon = Math.pow(10, -12);
        } else if (isPico(d)) {
            epsilon = Math.pow(10, -15);
        } else if (isFemto(d)) {
            epsilon = Math.pow(10, -16);
        }
        else{
            epsilon = 0;
        }
        return epsilon;
    }

    private boolean isPeta(double d) {
        return d >= Math.pow(10, 15);
    }

    private boolean isTera(double d) {
        return d >= Math.pow(10, 12) && (d < Math.pow(10, 15));
    }

    private boolean isGiga(double d) {
        return d >= Math.pow(10, 9) && (d < Math.pow(10, 12));
    }

    private boolean isMega(double d) {
        return d >= Math.pow(10, 6) && (d < Math.pow(10, 9));
    }

    private boolean isKilo(double d) {
        return d >= Math.pow(10, 3) && (d < Math.pow(10, 6));
    }

    private boolean isHecto(double d) {
        return d >= Math.pow(10, 2) && (d < Math.pow(10, 3));
    }

    private boolean isDeka(double d) {
        return d >= Math.pow(10, 1) && (d < Math.pow(10, 2));
    }

    private boolean isBaseUnit(double d) {
        return (d >= Math.pow(10, 0)) && d < Math.pow(10, 1);
    }

    private boolean isDeci(double d) {
        return (d >= Math.pow(10, -1)) && d < Math.pow(10, 0);
    }

    private boolean isCenti(double d) {
        return (d >= Math.pow(10, -2)) && d < Math.pow(10, -1);
    }

    private boolean isMilli(double d) {
        return (d >= Math.pow(10, -3)) && d < Math.pow(10, -2);
    }

    private boolean isMicro(double d) {
        return (d >= Math.pow(10, -6)) && d < Math.pow(10, -3);
    }

    private boolean isNano(double d) {
        return (d >= Math.pow(10, -9)) && d < Math.pow(10, -6);
    }

    private boolean isAngstrom(double d) {
        return (d >= Math.pow(10, -10)) && d < Math.pow(10, -9);
    }

    private boolean isPico(double d) {
        return (d >= Math.pow(10, -15)) && d < Math.pow(10, -10);
    }

    private boolean isFemto(double d) {
        return (d <= Math.pow(10, -16));
    }
}