using System.Collections.Generic;

namespace shilligama_blazor.Models;

// Resultado de una consulta paginada. Los totales vienen de los headers
// HTTP del back: X-Total-Count, X-Page, X-Page-Size, X-Total-Pages.
public class PagedResult<T>
{
    public List<T> Items { get; set; } = new();
    public int TotalCount { get; set; }
    public int Page { get; set; } = 1;
    public int PageSize { get; set; } = 20;
    public int TotalPages { get; set; } = 1;
}
