package org.tastytrash.spatialGUI.util;

/** Immutable local vertices. The original float operation order is preserved exactly. */
public final class CurvedPanelGeometry {
    private final int aspectBits, arcBits, segments;
    private final float[] vertices;

    public CurvedPanelGeometry(float aspect, float arcRadians, int segments) {
        this.aspectBits = Float.floatToRawIntBits(aspect);
        this.arcBits = Float.floatToRawIntBits(arcRadians);
        this.segments = segments;
        this.vertices = new float[(segments + 1) * 3];
        float radius = aspect / arcRadians;
        float halfArc = arcRadians * 0.5f;
        this.vertices[0] = radius * (float)Math.sin(-halfArc);
        this.vertices[1] = radius * (1.0f - (float)Math.cos(-halfArc));
        this.vertices[2] = 0.0f;
        for (int i = 1; i <= segments; i++) {
            float angle = -halfArc + arcRadians * ((float)i / (float)segments);
            this.vertices[i * 3] = radius * (float)Math.sin(angle);
            this.vertices[i * 3 + 1] = radius * (1.0f - (float)Math.cos(angle));
            this.vertices[i * 3 + 2] = (float)i / (float)segments;
        }
    }

    public boolean matches(float aspect, float arcRadians, int segments) {
        return this.aspectBits == Float.floatToRawIntBits(aspect)
                && this.arcBits == Float.floatToRawIntBits(arcRadians) && this.segments == segments;
    }
    public float x(int vertex) { return this.vertices[vertex * 3]; }
    public float z(int vertex) { return this.vertices[vertex * 3 + 1]; }
    public float u(int vertex) { return this.vertices[vertex * 3 + 2]; }
}
