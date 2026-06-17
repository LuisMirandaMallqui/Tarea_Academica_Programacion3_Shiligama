using System;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace shilligama_blazor.Shared;

/// <summary>
/// Serializa DateTime → Java como "yyyy-MM-ddTHH:mm:ss" (sin offset ni
/// microsegundos). Java usa @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss") sobre
/// LocalDateTime, que no admite fracción de segundos ni zona horaria.
///
/// Deserialización: acepta cualquier formato que DateTimeOffset.TryParse
/// reconozca (incluyendo el que devuelve Java sin offset) y devuelve la
/// parte local, lo que evita errores al leer respuestas de GlassFish.
/// </summary>
public sealed class JavaLocalDateTimeConverter : JsonConverter<DateTime>
{
    private const string WriteFormat = "yyyy-MM-dd'T'HH:mm:ss";

    public override DateTime Read(ref Utf8JsonReader reader, Type typeToConvert,
                                  JsonSerializerOptions options)
    {
        var s = reader.GetString();
        if (string.IsNullOrEmpty(s)) return DateTime.MinValue;

        if (DateTimeOffset.TryParse(s, out var dto))
            return dto.LocalDateTime;

        return DateTime.TryParse(s, out var dt) ? dt : DateTime.MinValue;
    }

    public override void Write(Utf8JsonWriter writer, DateTime value,
                               JsonSerializerOptions options)
        => writer.WriteStringValue(value.ToString(WriteFormat));
}

/// <summary>Versión nullable de <see cref="JavaLocalDateTimeConverter"/>.</summary>
public sealed class NullableJavaLocalDateTimeConverter : JsonConverter<DateTime?>
{
    private static readonly JavaLocalDateTimeConverter Inner = new();

    public override DateTime? Read(ref Utf8JsonReader reader, Type typeToConvert,
                                   JsonSerializerOptions options)
    {
        if (reader.TokenType == JsonTokenType.Null) return null;
        return Inner.Read(ref reader, typeof(DateTime), options);
    }

    public override void Write(Utf8JsonWriter writer, DateTime? value,
                               JsonSerializerOptions options)
    {
        if (value is null) writer.WriteNullValue();
        else Inner.Write(writer, value.Value, options);
    }
}
