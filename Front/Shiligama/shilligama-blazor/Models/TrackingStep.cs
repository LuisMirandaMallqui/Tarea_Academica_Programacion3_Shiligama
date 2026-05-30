namespace shilligama_blazor.Models;

public class TrackingStep
{
    public string Label { get; set; } = string.Empty;
    public DateTime? Time { get; set; }
    public bool IsCompleted { get; set; }
    public bool IsCancelled { get; set; }
}